package kr.or.ddit.exam;

public class Test {
	public static void main(String[] args) {
		String str1 = "유형욱";
		String str3 = "유형욱";

		System.out.println(str1.hashCode());
		System.out.println(str3.hashCode());

		String str2 = new String("유형욱");

		System.out.println("str1 hashCode : " + str1.hashCode());
		System.out.println(str1.equals(str2));

		System.out.println("str2 hashCode : " + str2.hashCode());
		System.out.println(str1 == str2);

		
		Object obj;
	}
}
