package MerkleTree;

import edu.cmu.andrew.xiumingl.SinglyLinkedList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class merkleTree {
	private static SinglyLinkedList listOfList = new SinglyLinkedList();

	public static SinglyLinkedList dataBlockTransformToHash(String fileDir)
			throws NoSuchAlgorithmException {

		SinglyLinkedList dataList = new SinglyLinkedList();
		SinglyLinkedList hashBase = new SinglyLinkedList();

		// read contents of file by line

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"))) {

			String cLine;
			while ((cLine = br.readLine()) != null) {
				dataList.addAtEndNode(cLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// force the first list to be even size
		if (dataList.countNodes() % 2 != 0) {
			Object copy = dataList.getLast();
			dataList.addAtEndNode(copy);
		}

		// add data blocks to the list
		listOfList.addAtEndNode(dataList);

		// transform data blocks into hash codes and add them to the list
		String s;
		Object o;
		for (int i = 0; i < dataList.countNodes(); i++) {
			o = dataList.getObjectAt(i);
			s = h(String.valueOf(o));
			hashBase.addAtEndNode(s);
		}

		listOfList.addAtEndNode(hashBase);

		return hashBase;
	}

	// generate the Merkle tree recursively
	public static String listGenerate(SinglyLinkedList hashBase) throws NoSuchAlgorithmException {

		SinglyLinkedList list = new SinglyLinkedList();
		int num = hashBase.countNodes();
		if (num == 1)
			return String.valueOf(hashBase.getLast());

		for (int a = 0, b = 1; a < num; a += 2, b += 2) {

			if (b == num)
				b = a;

			String s = String.valueOf(hashBase.getObjectAt(a)) + String.valueOf(hashBase.getObjectAt(b));
			list.addAtEndNode(h(s));

		}

		listOfList.addAtEndNode(list);
		return listGenerate(list);

	}

	// hashing method
	public static String h(String text) throws NoSuchAlgorithmException {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= 31; i++) {
			byte b = hash[i];
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	/*
	 * Test driver The file named "CrimeLatLonXY.csv" has the Merkle root that we seek£¬ which is:
	 * A5A74A770E0C3922362202DAD62A97655F8652064CCCBE7D3EA2B588C7E07B58
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

		// print out the Merkle root for four files
		System.out.println("The Merkle root of " + "smallFile.txt" + " is: ");
		SinglyLinkedList h1 = dataBlockTransformToHash("smallFile.txt");
		String merkleRoot1 = listGenerate(h1);
		System.out.println(merkleRoot1);

		System.out.println("The Merkle root of " + "CrimeLatLonXY.csv" + " is: ");
		SinglyLinkedList h2 = dataBlockTransformToHash("CrimeLatLonXY.csv");
		String merkleRoot2 = listGenerate(h2);
		System.out.println(merkleRoot2);

		System.out.println("The Merkle root of " + "CrimeLatLonXY1990_Size2.csv" + " is: ");
		SinglyLinkedList h3 = dataBlockTransformToHash("CrimeLatLonXY1990_Size2.csv");
		String merkleRoot3 = listGenerate(h3);
		System.out.println(merkleRoot3);

		System.out.println("The Merkle root of " + "CrimeLatLonXY1990_Size3.csv" + " is: ");
		SinglyLinkedList h4 = dataBlockTransformToHash("CrimeLatLonXY1990_Size3.csv");
		String merkleRoot4 = listGenerate(h4);
		System.out.println(merkleRoot4);

	}

}
