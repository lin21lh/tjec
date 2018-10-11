package com.freework.freedbm.util.listUtil.group;



public class GroupCol {
	private  int splitType[]=null;
	private  String colName=null;
	private Value value=null;
	private boolean isSum=true;
	
	public static GroupCol[] cols(String... colName){
		GroupCol[] groupCols=new GroupCol[colName.length];
		for (int i = 0; i < colName.length; i++) {
			groupCols[i]=col(colName[i]);
		}
		return groupCols; 
	}

	
	public static GroupCol col(Integer index,String name,boolean isData,int... splitType ){
		return new GroupCol(splitType==null||splitType.length<=0?null:splitType,name,index,isData);
	}
	public static GroupCol col(String unbindColName,boolean isData,int... splitType ){
		return new GroupCol(splitType==null||splitType.length<=0?null:splitType,unbindColName,isData);
	}
	public static GroupCol col(String unbindColName,int... splitType ){
		return new GroupCol(splitType==null||splitType.length<=0?null:splitType,unbindColName);
	}
	public GroupCol(int[] splitType,String name, Integer index,boolean isSum) {
			super();
			this.splitType = splitType;
			value=Value.value(index, Value.TYPE_SimpleMapValue);
			this.isSum=isSum;
			this.colName=name;
		}
	 GroupCol(int[] splitType, String colName) {
		super();
		this.splitType = splitType;
		this.colName = colName;
		value=Value.value(colName, Value.TYPE_MapValue);

	}
	 GroupCol(int[] splitType, String colName,boolean isSum) {
			super();
			this.splitType = splitType;
			this.colName = colName;
			this.isSum=isSum;
			value=Value.value(colName, Value.TYPE_MapValue);

		}
	public int[] getSplitType() {
		return splitType;
	}
	public void setSplitType(int[] splitType) {
		this.splitType = splitType;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}

	public boolean isSum() {
		return isSum;
	}
	
	 public Object getValue(Object obj){
		 return value.getValue(obj);
	 }
	 
	 public String toString(){
		 return colName;
	 }
		public boolean equals(Object anObject){
			if (this == anObject) {
			    return true;
			}
		//	System.out.println(""+anObject);
			if (anObject instanceof String) {
				return anObject.toString().toUpperCase().equals(this.colName.toUpperCase());
			}
			if (anObject instanceof GroupCol) {
				GroupCol g=(GroupCol)anObject;
				return g.colName.toUpperCase().equals(this.colName.toUpperCase());
			}
			return false;
			
			
		}
	 
}
