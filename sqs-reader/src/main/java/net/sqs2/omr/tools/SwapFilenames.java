package net.sqs2.omr.tools;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class SwapFilenames {

	public static void main(String[] args) {
		File directory = null;
		
		if(args.length == 0){
			// GUIを表示
			// このウィンドウにフォルダのアイコンがDrag and Dropされたら、
			// swapFilenames(そのフォルダ)を実行
			// swapFilenamesが正常に処理されたら、ダイアログボックスで終了を報告
		}else if(args.length == 1){
			
			directory = new File(args[0]);
		}else{
			System.out.println("Usage: java SwapFilenames <directory>");
			System.exit(0);
		}
		
		if(! directory.isDirectory()){
			System.out.println("This is not a directory!");
			System.exit(0);
		}
		try{
			swapFilenames(directory);
		}catch(IOException ex){
			System.err.println("ファイルシステムの書き込みに失敗しました");
		}
	}
	
	public static final String IMAGE_SUFFIX_ARRAY[] = new String[]{".png", ".tif", ".tiff", ".gif", ".jpg", ".jpeg"};
	
	/**
	 * 指定したディレクトリの中にある画像ファイル群について、
	 * 2*n+1番目のファイルの内容と、2*n番目のファイルの内容を入れ替える。
	 * @param directory 入れ替え対象の画像ファイルを含むディレクトリ
	 */
	public static void swapFilenames(File directory)throws IOException{
		// このdirectoryの下にあるファイルやサブディレクトリの名前の一覧をfileListに格納
		File[] fileList = directory.listFiles();
		// 名前の一覧をソート
		Arrays.sort(fileList, new Comparator<File>(){
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		swapFilenames(fileList);
	}

	/**
	 * 指定されたファイル（画像ファイルまたはフォルダ）について、
	 * 2*n+1番目のファイルの内容と、2*n番目のファイルの内容を入れ替える。
	 * 指定されたファイルがフォルダであった場合には、そのフォルダ以下を再帰的に処理。
	 * @param filelist 入れ替え対象となるファイルの配列
	 */
	public static void swapFilenames(File[] fileList) throws IOException {
		File tempFile = File.createTempFile("swapFilenames", ".image"); 
		File prevImageFile = null; 
		int imageFileIndex = 0;
		// filenameListの中身を filenameという変数に入れながら、ひとつづつ評価
		for(File file: fileList){
			//filenameからfileオブジェクトを作成
			
			if(file.isDirectory()){
				// fileがサブディレクトリだったら？
				File subDirectory = file;
				swapFilenames(subDirectory);
			}
			
			for(String suffix: IMAGE_SUFFIX_ARRAY){
				//IMAGE_SUFFIX_ARRAYの中身をsuffixという変数に入れながら、ひとつづつ評価
				if(file.getName().toLowerCase().endsWith(suffix)){
					//このファイルが画像ファイルとしての拡張子を持っている場合には…
					imageFileIndex++;
					if(imageFileIndex % 2 == 1){
						//奇数番目の画像ファイルのとき
						prevImageFile = file;
					}else{
						// 画像ファイルの親フォルダが、前の画像ファイルの親フォルダと一致しない場合には、
						// imageFileIndexを1に再設定して続行
						if(! prevImageFile.getParent().equals(file.getParent())){
							imageFileIndex = 1;
							prevImageFile = file;
							continue;
						}
						//偶数番目の画像ファイルのとき
						//ひとつ前のファイル（奇数番目のファイル）を、tmpFileに名前変更
						swapFilenames(tempFile, prevImageFile, file);
					}
				}
			}
		}
	}

	private static File swapFilenames(File tempFile, File prevImageFile, File curImageFile) {
		// prevImageFile と　fileの中身を入れ替え
		prevImageFile.renameTo(tempFile);
		curImageFile.renameTo(prevImageFile);
		tempFile.renameTo(curImageFile);
		return curImageFile;
	}
}
