package cpm1.ud2;

import java.math.BigInteger;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class POW {
	
	// Hash del nombre del alumno. En este caso: 'Jordi Maydeu Tarabal'
	public static String HASH_DEL_ALUMNO = "93aff03c77988c91a0ff71bfe5f942764ebb72a916743e4595b7ae0b434f92b9";

	// Hash del bloque anterior. Es este caso: "Introducción a las Criptomonedas y
	// Smart Contracts";
	public static String HASH_PREVIO = "56d52878eb8600c7a4c585af9f45a3511a7e1db425fe921001924f11643de4b6";

	// Hash transaccion 1 para calcular raiz de Merkle
	public static String HASH_TRANSACCION_1 = "9c7ab5a8c9ee62fd6a8b2ea0d83eba45cd92fa8fd950a9616d93bb6bd5f6c94e";

	// Hash transaccion 2 para calcular raiz de Merkle
	public static String HASH_TRANSACCION_2 = "8792106e4ed2fe7ae0f7e737f3652dcf555a8cb4ed652eee568d5be3174c81b0";

	// Dificultad
	public static String DIFICULTAD = "1e0fffff";

	// Nonce inicial
	public static int NONCE_INICIAL = 0;

	// Version
	public static int VERSION = 1;
	
	//Mascara
	private static final int MASCARA_BYTE = 0xFF;
	 

	public static void main(String[] args) {

		// HASH DEL BLOQUE GENERADO
		String hash = "";

		long time_start, time_end;
		time_start = System.currentTimeMillis();
				
		try {
						
			if(ByteOrder.LITTLE_ENDIAN.equals(ByteOrder.nativeOrder())) {
				System.out.println("ESTAMOS EN UNA MAQUINA:" + ByteOrder.LITTLE_ENDIAN.toString());
			}else if (ByteOrder.BIG_ENDIAN.equals(ByteOrder.nativeOrder())) {
				System.out.println("ESTAMOS EN UNA MAQUINA:" + ByteOrder.BIG_ENDIAN.toString());
			}
			
			// Generamos 4 bytes en hexadecimal de la version en little endian
			String version = bytesToHex(intToByteArray(VERSION, ByteOrder.LITTLE_ENDIAN));
			
			// Calculamos Raiz de Merkle concatenando los hash's de las transacciones 1 y 2.
			String raizMerkle = digest(HASH_TRANSACCION_1.concat(HASH_TRANSACCION_2));

			// Recuperamos tiempo actual en segundos codificado en little Endian
			String timestamp = bytesToHex(intToByteArray((int)(System.currentTimeMillis() / 1000), ByteOrder.LITTLE_ENDIAN));
						

			System.out.println("VERSION:              " + version);
			System.out.println("HASH PREVIO:          " + HASH_PREVIO);
			System.out.println("RAIZ DE MERKLE:       " + raizMerkle);
			System.out.println("TIMESTAMP:            " + timestamp);
			System.out.println("DIFICULTAD:           " + DIFICULTAD);
			System.out.println("HASH DEL ALUMNO:      " + HASH_DEL_ALUMNO);

			String coeficienteHex = DIFICULTAD.substring(2);

			String exponenteHex = DIFICULTAD.substring(0, 2);
			int exponenteInt = Integer.decode("0x" + exponenteHex);
			
			String pattern = "%0"+String.valueOf(exponenteInt*2-6)+"x";
			String targetHex = coeficienteHex + String.format(pattern, 0);
			BigInteger targetBigI = new BigInteger(targetHex,16);
			
			System.out.println("TARGET DIFFICULT:     " + targetHex);

			for (int i = NONCE_INICIAL; i < Integer.MAX_VALUE; i++) {

				// Generamos 4 bytes en hexadecimal del nonce
				String nonceHex = bytesToHex(intToByteArray(i, ByteOrder.LITTLE_ENDIAN));

				StringBuilder data = new StringBuilder();
				data.append(version).append(HASH_PREVIO).append(raizMerkle).append(timestamp).append(DIFICULTAD)
						.append(HASH_DEL_ALUMNO).append(nonceHex);

				// Calculamos hash del bloque para un nonce
				hash = digest(data.toString());

				if( i == NONCE_INICIAL) {
					System.out.println("HASH[nonce=" + nonceHex + "]: " + hash);
				}
					 
				
				BigInteger hashInt = new BigInteger(hash, 16);

				if (hashInt.compareTo(targetBigI) == -1) {
					System.out.println("HASH[nonce=" + nonceHex + "]: " + hash);
					break;
				}

			}

		} catch (Exception e1) {
			System.out.println("ERROR: " + e1.getMessage());
			System.exit(1);
		} finally {

			time_end = System.currentTimeMillis();
			System.out.println("EL MINADO HA DURADO: " + (time_end - time_start)/1000 + " seconds");
			System.exit(0);
		}

	}
	
	/**
     * Calcula el doble hash SHA-256 de una cadena de texto
     * 
     * @param data string a calcular
     * @return hash en hexadecimal
     */
	public static String digest(String data) throws NoSuchAlgorithmException {
		return hash256(hash256(data));
	}
	
	
	/**
     * Calcula el hash SHA-256 de una cadena de texto
     * 
     * @param data string a calcular
     * @return hash en hexadecimal
     */
    private static String hash256(String data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(data.getBytes());
		return bytesToHex(md.digest());
	}

    /**
     * Codifica en hexadecimal un array de bytes
     * 
     * @param valor array de bytes a convertir
     * @return valor codificado en hexadecimal
     */
	public static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes)
			result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}
	
	
	/**
     * Convierte int a un byte array respetando el orden asignado
     * 
     * @param valor valor a convertir
     * @param endianness orden a utilizar en la conversion
     * @return array de bytes equivalentes a valor
     */
    public static byte[] intToByteArray(int valor, ByteOrder endianness) {
        byte[] resultado;
        byte b3 = (byte) ((valor >> 24) & MASCARA_BYTE);
        byte b2 = (byte) ((valor >> 16) & MASCARA_BYTE);
        byte b1 = (byte) ((valor >> 8 ) & MASCARA_BYTE);
        byte b0 = (byte) (valor & MASCARA_BYTE);
        if (ByteOrder.LITTLE_ENDIAN.equals(endianness)) {
        	resultado = new byte[]{b0, b1, b2, b3};
        } else {
        	resultado = new byte[]{b3, b2, b1, b0};
        }
        return resultado;
    }
 
    /**
     * Convierte un array de bytes a un int respetando el orden asignado
     * 
     * @param valor array de bytes a convertir
     * @param endianness orden a utilizar en la conversion
     * @return valor equivalente despues de la conversion
     */
    public static int byteArrayToInt(byte[] valor, ByteOrder endianness) {
        
    	if (valor.length < 4) {
            throw new ArrayIndexOutOfBoundsException(valor.length);
        }
        
    	int a, b, c, d;
    	if (ByteOrder.LITTLE_ENDIAN.equals(endianness)) {
        	a = (valor[3] & MASCARA_BYTE) << 24;
            b = (valor[2] & MASCARA_BYTE) << 16;
            c = (valor[1] & MASCARA_BYTE) << 8;
            d = valor[0] & MASCARA_BYTE;
        } else {
        	a = (valor[0] & MASCARA_BYTE) << 24;
            b = (valor[1] & MASCARA_BYTE) << 16;
            c = (valor[2] & MASCARA_BYTE) << 8;
            d = valor[3] & MASCARA_BYTE;
        }
        return a | b | c | d;
    }
 
    /**
     * Convierte long a un byte array respetando el orden asignado
     * 
     * @param valor valor a convertir
     * @param endianness orden a utilizar en la conversion
     * @return array de bytes equivalentes a valor
     */
    public static byte[] longToByteArray(long valor, ByteOrder endianness) {
        byte[] resultado;
        byte b7 = (byte) ((valor >> 56) & MASCARA_BYTE);
        byte b6 = (byte) ((valor >> 48 ) & MASCARA_BYTE);
        byte b5 = (byte) ((valor >> 40) & MASCARA_BYTE);
        byte b4 = (byte) ((valor >> 32) & MASCARA_BYTE);
        byte b3 = (byte) ((valor >> 24) & MASCARA_BYTE);
        byte b2 = (byte) ((valor >> 16) & MASCARA_BYTE);
        byte b1 = (byte) ((valor >> 8 ) & MASCARA_BYTE);
        byte b0 = (byte) (valor & MASCARA_BYTE);
        if (ByteOrder.LITTLE_ENDIAN.equals(endianness)) {
        	resultado = new byte[]{b0, b1, b2, b3, b4, b5, b6, b7};
        } else {
        	resultado = new byte[]{b7, b6, b5, b4, b3, b2, b1, b0};
        }
        return resultado;
    }
 
    /**
     * Convierte un array de bytes a un long respetando el orden asignado
     * 
     * @param valor array de bytes a convertir
     * @param endianness orden a utilizar en la conversion
     * @return valor equivalente despues de la conversion
     */
    public static long byteArrayToLong(byte[] valor, ByteOrder endianness) {
        if (valor.length < 8 ){
            throw new ArrayIndexOutOfBoundsException(valor.length);
        }
        long a, b, c, d, e, f, g, h;
        if (ByteOrder.LITTLE_ENDIAN.equals(endianness)) {
        	a = (long) (valor[7] & MASCARA_BYTE) << 56;
            b = (long) (valor[6] & MASCARA_BYTE) << 48;
            c = (long) (valor[5] & MASCARA_BYTE) << 40;
            d = (long) (valor[4] & MASCARA_BYTE) << 32;
            e = (long) (valor[3] & MASCARA_BYTE) << 24;
            f = (long) (valor[2] & MASCARA_BYTE) << 16;
            g = (long) (valor[1] & MASCARA_BYTE) << 8;
            h = (long) (valor[0] & MASCARA_BYTE);
        	
        } else {
        	a = (long) (valor[0] & MASCARA_BYTE) << 56;
            b = (long) (valor[1] & MASCARA_BYTE) << 48;
            c = (long) (valor[2] & MASCARA_BYTE) << 40;
            d = (long) (valor[3] & MASCARA_BYTE) << 32;
            e = (long) (valor[4] & MASCARA_BYTE) << 24;
            f = (long) (valor[5] & MASCARA_BYTE) << 16;
            g = (long) (valor[6] & MASCARA_BYTE) << 8;
            h = (long) (valor[7] & MASCARA_BYTE);
        }
        return a | b | c | d | e | f | g | h;
    }
 

}
