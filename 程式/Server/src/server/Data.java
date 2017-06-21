package server;

public class Data {
	private String[] name={"abcdefg","aaaaa","abca"};
	private String[] password1={"123456","12345","1234"};
	int count=0;
	public void add(int count){
		this.count=this.count+count;
		if (this.count>=3)this.count=0;
	}
	public int find() {
		// TODO Auto-generated constructor stub		
		return count;
	}
	public void clear(int count){
		this.count=0;
	}

}
