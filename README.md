# CPM1
(CPM1) Criptomoneda y Smart Contracts

Algoritmo mediante el cual se obtiene un hash de encabezado de bloque válido mediante el algoritmo de prueba de trabajo (Proof of Work) a partir de los datos que se muestran a continuación:

HASH_DEL_ALUMNO = "93aff03c77988c91a0ff71bfe5f942764ebb72a916743e4595b7ae0b434f92b9"

HASH_PREVIO = "56d52878eb8600c7a4c585af9f45a3511a7e1db425fe921001924f11643de4b6"

HASH_TRANSACCION_1 = "9c7ab5a8c9ee62fd6a8b2ea0d83eba45cd92fa8fd950a9616d93bb6bd5f6c94e"

HASH_TRANSACCION_2 = "8792106e4ed2fe7ae0f7e737f3652dcf555a8cb4ed652eee568d5be3174c81b0"

DIFICULTAD = "1e0fffff"

NONCE_INICIAL = 0

VERSION = 1




			// Generamos 4 bytes en hexadecimal de la version en little endian
			
			// Calculamos Raiz de Merkle concatenando los hash's de las transacciones 1 y 2.

			// Recuperamos tiempo actual en segundos codificado en little Endian

//Calculamos la difcultad objectivo separando coeficiente y exponente
 
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

		
