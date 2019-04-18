package MyJenaProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ProjectDataReader2 {
	private List<String> kind = new ArrayList<String>();
	private List<String> object = new ArrayList<String>();
	private List<String> source = new ArrayList<String>();
	private List<String> target = new ArrayList<String>();
	private int step = 0;
	
	public ProjectDataReader2(String fileName) {
		String[] tmp;
		String line="";
		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			while ((line=br.readLine())!=null) {
				tmp = line.split(",");
				kind.add(tmp[0]);
				object.add(tmp[1]);
				source.add(tmp[2]);
				target.add(tmp[3]);
				step++;
			}
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getKind(int index){
		return kind.get(index);
	}
	public String getObject(int index){
		return object.get(index);
	}
	public String getSource(int index){
		return source.get(index);
	}
	public String getTarget(int index){
		return target.get(index);
	}
	
	public int getTotalStep(){
		return step;
	}
}
