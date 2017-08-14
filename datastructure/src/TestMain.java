
public class TestMain {

	
	static int a= 50;
	static int b = 10;
	
	
	void TestMain(){}
	
	public static void main(String[] args) {
		System.out.println(decryptPassword("339739493893390939493909"));
		//System.out.println(decryptPassword("39893733363738933597381334373725"));
		 
		/*TestMain tm = new TestMain();
		tm.a += -- tm.b;
		System.out.println(tm.a);*/
	}
	public void  r()
	{
		System.out.println(a);
	}
	
	public static String decryptPassword(String strDePwd) {
		int len1, k, l, i1, j1, k1, l1, msgLenCtr = 0;
		char c, c1, c2, c3, c4;
		String s, s1, s2, s3, s4, s5, s6, unScrambledMessage = "";
		String tempL = "";
		//String La = StringConstants.EMPTY_STRING;

		len1 = strDePwd.length();

		/*----- Avoid whitespaces -----*/
		for (int j = 0; j < len1; j++) {
			c = strDePwd.charAt(j);
			if (Character.isWhitespace(c)) {
				j++;
			} else {
				s = String.valueOf(c);
				tempL = tempL + s;
			}
		}

		/* ----- Decoding the first Char ------- */
		strDePwd = tempL;
		len1 = strDePwd.length();
		tempL = "";
		c1 = strDePwd.charAt(0);
		s1 = String.valueOf(c1);
		k = Integer.parseInt(s1);
		s2 = strDePwd.substring(1, k + 1);
		l = Integer.parseInt(s2);
		i1 = ((((l + 11) / 2 + 10) / 2) - 7 - 4) / 2;
		c2 = (char) i1;
		s3 = String.valueOf(c2);
		msgLenCtr = k + 1;
		unScrambledMessage = unScrambledMessage + s3;

		/* -------- Decoding the rest ---------- */
		for (; msgLenCtr < len1; msgLenCtr = msgLenCtr + j1 + 1) {
			c3 = strDePwd.charAt(msgLenCtr);
			s4 = String.valueOf(c3);
			j1 = Integer.parseInt(s4);
			s5 = strDePwd.substring(msgLenCtr + 1, msgLenCtr + j1 + 1);
			k1 = Integer.parseInt(s5);
			l1 = ((((k1 + 11) / 2 + 10) / 2) - 7 - 4) / 2;
			c4 = (char) l1;
			s6 = String.valueOf(c4);
			unScrambledMessage = unScrambledMessage + s6;
		}

		return unScrambledMessage;
	}
}
