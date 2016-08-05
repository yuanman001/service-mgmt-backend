package testapi;

public class split {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String a = "Adfs|ffff|KEICsE|qweEF";
		String[] b = a.split("\\|");
		System.out.println(b[1]);
	}

}
