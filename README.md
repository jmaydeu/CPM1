(CPM1) Criptomoneda y Smart Contracts

Algoritmo mediante el cual se obtiene un hash de encabezado de bloque válido mediante el algoritmo de prueba de trabajo (Proof of Work) a partir de los datos que se muestran a continuación:

HASH_DEL_ALUMNO = "93aff03c77988c91a0ff71bfe5f942764ebb72a916743e4595b7ae0b434f92b9"

HASH_PREVIO = "56d52878eb8600c7a4c585af9f45a3511a7e1db425fe921001924f11643de4b6"

HASH_TRANSACCION_1 = "9c7ab5a8c9ee62fd6a8b2ea0d83eba45cd92fa8fd950a9616d93bb6bd5f6c94e"

HASH_TRANSACCION_2 = "8792106e4ed2fe7ae0f7e737f3652dcf555a8cb4ed652eee568d5be3174c81b0"

DIFICULTAD = "1e0fffff"

NONCE_INICIAL = 0

VERSION = 1


1.- Generar 4 bytes en hexadecimal de la version en little endian
		
2.- Calcular la Raiz de Merkle concatenando los hash's de las transacciones 1 y 2.

3.- Recuperar el tiempo actual en segundos codificado en little Endian. Con java utilizamos el comando " System.currentTimeMillis()".
Para este campo tengo unas cuantas dudas, ¿Existe una 

4.- Calculamos la difcultad objectivo separando coeficiente y exponente

5.- Realizar un bucle des de el NONCE inicial (0) hasta Integer.MAX_VALUE.

     5.1 -  Para cada NONCE, Generar 4 bytes en hexadecimal en formato little endian

	 5.2 - Concatenar los siguientes campos con el mismo orden: 
	         version+ HASH_PREVIO + raizMerkle + timestamp + DIFICULTAD + HASH_DEL_ALUMNO + nonceHex

	 5.3 - Calcular el hash-256 doble del bloque para el nonce

	 5.4 - Comparar si el hash generado es menor que la dificultad. En caso afirmativo, ya tenemos un hash válido para el bloque. 



6.- Ejemplo de una ejecución del código:


ESTAMOS EN UNA MAQUINA:LITTLE_ENDIAN
VERSION:              01000000
HASH PREVIO:          56d52878eb8600c7a4c585af9f45a3511a7e1db425fe921001924f11643de4b6
RAIZ DE MERKLE:       4767faeab5282cc0574969951e627e1f6d0209bff934511e193ba5087bc66fad
TIMESTAMP:            6279375a
DIFICULTAD:           1e0fffff
HASH DEL ALUMNO:      93aff03c77988c91a0ff71bfe5f942764ebb72a916743e4595b7ae0b434f92b9
TARGET DIFFICULT:         0fffff000000000000000000000000000000000000000000000000000000
HASH[nonce=00000000]: 67bf458e8621ef5cb89ea02419429b05b0932f8f02cb9073a1498554c90343d7
HASH[nonce=ab930e00]: 000002dc156bca8408f21068883b476d022e6f5cbd467fad283cf080d5d0e531
EL MINADO HA DURADO: 6 seconds

