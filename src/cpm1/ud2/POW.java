package cpm1.ud2;

import java.math.BigInteger;
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

	public static void main(String[] args) {

		// HASH DEL BLOQUE GENERADO
		String hash = "";

		long time_start, time_end;
		time_start = System.currentTimeMillis();

		try {

			// Generamos 4 bytes en hexadecimal de la version
			String version = String.format("%08x", VERSION);

			// Calculamos Raiz de Merkle concatenando los hash's de las transacciones 1 y 2.
			String raizMerkle = digest(HASH_TRANSACCION_1.concat(HASH_TRANSACCION_2));

			// Recuperamos tiempo actual en segundos
			String timestamp = Long.toString(System.currentTimeMillis() / 1000, 16);

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
				String nonceHex = String.format("%08x", i);

				StringBuilder data = new StringBuilder();
				data.append(version).append(HASH_PREVIO).append(raizMerkle).append(timestamp).append(DIFICULTAD)
						.append(HASH_DEL_ALUMNO).append(nonceHex);

				// Calculamos hash del bloque para un nonce
				hash = digest(data.toString());

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
	
	public static String digest(String data) throws NoSuchAlgorithmException {
		return hash256(hash256(data));
	}
	
    private static String hash256(String data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(data.getBytes());
		return bytesToHex(md.digest());
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes)
			result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}

}
