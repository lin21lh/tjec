package com.freework.freedbm.cfg.fieldcfg;

/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class Like implements WhereOperators{
	
	public static final Like ALL_LIKE=new Like();
	public static final Like LEFT_LIKE=new Like(Location.left);
	public static final Like RIGHT_LIKE=new Like(Location.right);
		   public String wildcard = "%";
		   public Location location = Location.all;
		   public Like(){}
		   public Like(Location location){this.setLocation(location);}
		   public Like(Location location,String wildcard){this.setLocation(location);setWildcard(wildcard);}

		   public String getWildcard() {
			return wildcard;
		}

		public void setWildcard(String wildcard) {
			this.wildcard = wildcard;
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public enum Location {left, right,all}

		@Override
		public String getOperator() {
			return "like";
		}
	
}
