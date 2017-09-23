package org.zagelnews.constants;


public interface FeedsConstants {
	Long FEED_EDIT_EXPIRY = 24L;
	int TEXT_LIMIT = 300;

	
	public interface FeedAuthorTypeConst {	
		public static final Integer USER = 0;
		public static final Integer GROUP= 1;
	}
	
	public interface NotificationTypesConst {
		public static final Integer FEEDS = 1;
		public static final Integer GROUPS = 2;
	}
	
	public interface NotificationClassesConst {
		public static final Integer FEED_POSITIVE_VOTE = 1;
		public static final Integer FEED_NEGATIVE_VOTE = 2;
		public static final Integer FEED_COMMENTS = 3;
	}
	
	public interface VoteTypes{
		public static final Integer LIKE = 1;
		public static final Integer DISLIKE = -1;
	}
}
