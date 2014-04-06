	public class BoardMember{
		public String organization;
		public String role;
		public String title;
		public String from;
		public String to;
		
		public BoardMember(){}
		
		public BoardMember(String org, String role, String title, String from, String to) {
			this.organization = org;
			this.role = role;
			this.title = title;
			this.from = from;
			this.to = to;
		}
	}