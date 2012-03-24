package net.sqs2.image;
import net.sqs2.image.ImageSilhouetteExtract;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
public class ImageSilhouetteExtractTest extends TestCase {
	final static boolean _ = false; 
	final static boolean o = true;
	final static String LABEL = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	final static int LABEL_LENGTH = LABEL.length();
	boolean[] src = new boolean[]{
			o,_,_,_,o,_,_,_,_,_, _,_,o,_,_,_,_,o,_,_, _,_,_,_,_,o,_,_,_,_, _,_,o,o,o,o,o,o,o,_, _,o,_,_,_,_,o,o,o,o,
			_,_,_,_,_,o,_,_,_,_, _,o,_,_,_,o,o,o,o,o, _,_,_,_,o,_,_,_,_,_, _,_,_,_,_,_,o,_,o,_, _,o,o,o,o,o,o,o,o,o,
			o,_,_,_,_,_,o,_,_,_, o,_,_,_,_,_,_,o,_,_, _,_,_,o,_,_,_,_,_,_, _,_,_,_,_,_,_,_,_,_, _,_,_,_,_,_,_,_,_,_,
			o,_,_,_,_,_,_,o,_,o, _,_,_,_,_,_,_,_,_,_, _,_,_,_,_,_,o,_,_,_, _,o,o,o,o,o,o,o,o,o, _,_,_,_,o,_,o,_,o,_,
			o,o,o,o,o,o,_,_,o,_, _,_,_,o,o,o,o,o,o,o, _,_,_,_,_,_,_,o,_,_, _,o,_,_,_,_,_,_,_,o, _,_,_,_,_,o,_,o,_,o,
			o,_,_,_,_,_,_,o,_,o, _,_,_,o,_,_,_,_,_,o, _,_,_,_,_,_,_,_,o,_, _,o,_,o,o,o,o,o,_,o, _,_,_,_,o,_,o,_,o,_,
			_,o,_,_,_,_,o,_,_,_, o,_,_,o,_,o,o,o,_,o, _,_,_,_,_,_,o,_,_,_, _,o,_,o,_,_,_,o,_,o, _,_,_,_,_,_,_,_,_,_,
			_,_,o,_,_,o,_,_,o,_, _,o,_,o,_,o,o,o,_,o, _,_,_,_,_,_,_,_,_,_, _,o,_,o,_,o,o,o,_,o, _,_,_,_,o,_,o,_,o,_,
			_,_,_,o,_,_,_,o,_,_, _,_,_,o,_,_,_,_,_,o, _,_,_,o,_,_,_,o,_,_, _,o,_,o,_,_,_,_,_,o, _,_,_,_,_,o,_,o,_,o,
			_,_,_,_,o,o,o,_,_,_, _,_,_,o,o,o,o,o,o,o, _,_,_,_,_,o,_,_,_,_, _,o,_,o,o,o,o,o,o,o, _,_,_,_,o,_,o,_,o,_,
	};


	public void testConst(){
		ImageSilhouetteExtract ise = new ImageSilhouetteExtract(src, 50, 10);

		String[] expected = { 
				"a   b       b    d       e      fffffff  g    gggg",
				"     b     b   ddddd    e           f f  ggggggggg",
				"j     b   b      d     e                          ",
				"j      b b                k    lllllllll    m m m ",
				"jjjjjj  b    ppppppp       k   l       l     m m m",
				"j      b b   p     p        k  l lllll l    m m m ",
				" j    b   b  p rrr p      s    l l   l l          ",
				"  j  b  j  b p rrr p           l l lll l    v v v ",
				"   j   j     p     p   y   z   l l     l     v v v",
				"    jjj      ppppppp     A     l lllllll    v v v "};
		int width = expected[0].length();
		int height = expected.length;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int p = ise.getSilhouetteIndexArray()[x+y*width];
				char c = getLabelChar(p);
				try{
					assertEquals(expected[y].charAt(x), c);
				}catch(AssertionFailedError err){
					System.err.println("x,y="+x+","+y+"  "+expected[y].charAt(x)+" != "+c);
				}
			}
		}

	}
	

	void printHeader(int length){
		for(int i = 0; i < length; i++){
			System.out.print(i%10);
		}
		System.out.println();
	}
	
	void printBody(int[] table){
		for(int i = 0; i < table.length; i++){
			System.out.print(table[i]);
		}
		System.out.println();
	}

	private char getLabelChar(int p) {
	    char c;
	    if(p < LABEL_LENGTH){
	    	c = LABEL.charAt(p);
	    }else{
	    	c = '*';
	    }
	    return c;
    }
}
