package myTest;

public class myTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] array = {{1,2,3},{4},{5,6,7,8}};
		int[][] array2;
		
		for(int i = 0; i < array.length ; i++){
			for(int j = 0; j < array[i].length;j++){
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
		array2 = array;
		for(int i = 0; i < array2.length ; i++){
			for(int j = 0; j < array2[i].length;j++){
				System.out.print(array2[i][j] + " ");
			}
			System.out.println();
		}
		

	}

}
