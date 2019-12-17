package chat.model;

import java.io.Serializable;

import chat.model.header.Header;
import chat.model.payload.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Packet implements Serializable {

	private static final long serialVersionUID = 1L;

	private Header header;

	private Payload payload;

	public static void print(String comment, char[] array) {
		System.out.print(comment);
		for (char word : array)
			System.out.print(word);
		System.out.println();
	}
	
	public static void print(String comment, byte word) {
		System.out.println(comment + word);
	}
	
	public static char[] createField(String value, int size) {
		char[] tmp = new char[size];
		for(int i = 0; i < size; i++) 
			tmp[i] = i < value.length() ? value.charAt(i) : ' ';
		
		return tmp;
	}

}
