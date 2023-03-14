package com.example.final_case_social_web.common;

public class Constants {

    public static class DataField {
        public static final String CONTENT = "content";
        public static final String WORK = "work";
    }

    public static class Link {
        public static final String CHECK_LINK = "https://";
        public static final String CHECK_LINK_2 = "http:";
    }

    public static class IdCheck {
        public static final String ID_ADMIN = "idAdmin";

        public static final String ID_USER = "idUser";
        public static final String ID_POST = "idPost";
        public static final String ID_COMMENT = "idComment";
        public static final String ID_ANSWER_COMMENT = "idAnswerComment";
        public static final String ID_SORT_NEW = "idSortNew";
        public static final String ID_SAVE = "idSaved";
        public static final String ID_EVENT = "idEvent";
        public static final String ID_IMAGE = "idImage";
        public static final String ID_GROUP = "idGroup";
        public static final String ID_THE_GROUP = "idTheGroup";
        public static final String ID_CONVERSATION = "idConversation";
        public static final String ID_SENDER = "idSender";
        public static final String ID_RECEIVER = "idReceiver";
        public static final String ID_ADMIN_GROUP = "idAdminGroup";
        public static final String ID_THE_GROUP_POST = "idTheGroupPost";
        public static final String ID_MESSAGE = "idMessenger";
        public static final String ID_USER_DESCRIPTION = "idUserDescription";
    }

    public static class Roles {
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_USER = "ROLE_USER";
    }

    public static class ImageDefault {
        public static final String DEFAULT_IMAGE_AVATAR_MALE = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2Favatar-gian.jpg?alt=media&token=84926670-338a-47e4-930e-708e0bf925ec";
        public static final String DEFAULT_IMAGE_AVATAR_LGBT = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2FavatarLGBT.jpg?alt=media&token=ac689fcc-0f8d-4f74-948d-b8957d807bb2";
        public static final String DEFAULT_IMAGE_AVATAR_FEMALE = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2Fhinh-avatar-trang.jpg?alt=media&token=eb8d6d41-ace4-4a9c-97ec-b360bfd1cbcf";
        public static final String DEFAULT_BACKGROUND = "assets/images/hinhnen.png";
        public static final String DEFAULT_BACKGROUND_2 = "assets/images/defaultAva.png";
        public static final String DEFAULT_BACKGROUND_3 = "assets/images/face-map_ccexpress.png";
        public static final String DEFAULT_IMAGE_SHORT_NEW = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2F1706_hinh-nen-dien-thoai-4k62.jpg?alt=media&token=92af4669-624c-4193-bdf4-9d27a471fdbe";
        public static final String DEFAULT_AVATAR_GROUP = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2Fbg2.jpg?alt=media&token=ed98cff4-0ddd-402b-81f9-a18db3b3c50e";
        public static final String DEFAULT_COVER_GROUP = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2Fbg1.jpg?alt=media&token=4a648a76-8187-4598-8f54-e131b73d16c8";
    }

    public static class ListAvatarDefault {
        public static final String DEFAULT_AVATAR_1 = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2Fav1.jpg?alt=media&token=70fb7dd3-c7fa-4264-b7b8-025f6eaca942";
        public static final String DEFAULT_AVATAR_2 = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2Fav2.jpg?alt=media&token=2bd25352-a9e5-4677-98dd-429bbd8d6675";
        public static final String DEFAULT_AVATAR_3 = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2Fav3.jpg?alt=media&token=ed5d02c7-95ff-4271-b677-9504282df1cb";
        public static final String DEFAULT_AVATAR_4 = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2Fav4.jpg?alt=media&token=c5ed9aec-d4c6-4f14-920e-39ee1cd5f4cc";
        public static final String DEFAULT_AVATAR_5 = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2Fav5.jpg?alt=media&token=c1d964e4-457c-42d5-8ff5-6ba499304558";
        public static final String DEFAULT_AVATAR_6 = "https://firebasestorage.googleapis.com/v0/b/test2-d8f2d.appspot.com/o/RoomsImages%2Fav6.jpg?alt=media&token=68da0f00-cc2b-4c9d-81e7-7c7f04bbb614";
    }

    public static class GroupStatus {
        public static final String STATUS_GROUP_PENDING = "Pending approval";
        public static final String STATUS_GROUP_APPROVED = "Approved";
        public static final String STATUS_GROUP_REFUSE = "Refuse";
        public static final String MANAGEMENT = "Group management";
    }

    public static class ConversationStatus {
        public static final String STATUS_TWO = "Two people";
        public static final String STATUS_ONE = "One people";
        public static final String STATUS_NO = "No people";
    }

    public static class FollowPeople {
        public static final String FOLLOW = "Follow";
        public static final String UNFOLLOW = "Unfollow";
        public static final String WATCHING = "Watching";
    }

    public static class Excel {

        public static final String HEADER = "Header";
        public static final String CONTENT = "Content";
        public static final String USER_SHEET = "Users";

        public static final String[] EXCEL_USER_HEADER = {
                "Full Name",
                "Created Date",
                "Updated Date",
        };
    }

    public static class Notification {
        public static final String STATUS_SEEN = "Seen";
        public static final String STATUS_NOT_SEEN = "Not seen";
        public static final String TYPE_POST = "Post";
        public static final String TYPE_CONVERSATION = "Conversation";
        public static final String TYPE_GROUP_POST = "Group post";
        public static final String TYPE_GROUP = "Join group";
        public static final String TYPE_SYSTEM = "System";
        public static final String TYPE_COMMENT = "Comment";
        public static final String TYPE_ANSWER_COMMENT = "Answer comment";
        public static final String TYPE_FRIEND = "Friend";
        public static final String TITLE_SEND_REQUEST_FRIEND = "đã gửi lời mời kết bạn";
        public static final String TITLE_AGREE_FRIEND = "đã đồng ý kết bạn";
        public static final String TITLE_SEND_MESSAGE = "đã gửi tin nhắn cho bạn";
        public static final String TITLE_LIKE_POST = "đã thích bài viết của bạn";
        public static final String TITLE_HEART_POST = "đã thả tim bài viết của bạn";
        public static final String TITLE_DISLIKE_POST = "không thích bài viết của bạn";
        public static final String TITLE_LIKE_COMMENT = "đã thích bình luận của bạn";
        public static final String TITLE_DISLIKE_COMMENT = "không thích bình luận của bạn";
        public static final String TITLE_COMMENT = "đã bình luận bài viết của bạn";
        public static final String TITLE_ANSWER_COMMENT = "đã trả lời bình luận của bạn";
        public static final String TITLE_APPROVE = "đã duyệt bài viết của bạn";
        public static final String TITLE_REJECT = "đã từ chối duyệt bài viết của bạn";
        public static final String TITLE_APPROVE_JOIN_GROUP = "đã đồng ý cho bạn tham gia nhóm";
        public static final String TITLE_REQUEST_JOIN_GROUP = "muốn tham gia nhóm của bạn";
        public static final String TITLE_REQUEST_CREATE_POST = "muốn đăng bài viết trong nhóm của bạn";
        public static final String TITLE_REJECT_JOIN_GROUP = "đã từ chối đề nghị tham gia nhóm của bạn";
    }

    public static class GroupType {
        public static final String TYPE_MUSIC = "Âm nhạc";
        public static final String TYPE_ENTERTAINMENT = "Giải trí";
        public static final String TYPE_MOVIE = "Phim";
        public static final String TYPE_CLOTHES = "Quần áo";
        public static final String TYPE_FOOTWEAR = "Giày dép";
        public static final String TYPE_JOB = "Việc làm";
        public static final String TYPE_FIX = "Sửa chữa";
        public static final String TYPE_APPLIANCES = "Đồ gia dụng";
        public static final String TYPE_GAME = "Game";
    }

    public static final String MESSAGE_STRIKE_THROUGH = "-------------------------------------------------------------------------";
    public static final String BLANK = "";
    public static final String STATUS_DELETE = "Delete";
    public static final String STATUS_PUBLIC = "Public";
    public static final String STATUS_PRIVATE = "Private";
    public static final String REQUEST = "Request";
    public static final String RESPONSE = "Response";

    public static final String MESSAGE_RECALL = "Message recall";
    public static final String FRIEND = "Friend";
    public static final String NO_FRIEND = "No friend";
    public static final String WAITING = "Waiting";
    public static final String BLOCKED = "Blocked";
    public static final String UN_BLOCKED = "Unblocked";
    public static final String STATUS_LOCK = "Lock";
    public static final String STATUS_BANED = "Banned";
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_SAVED = "Saved";
    public static final String GENDER_DEFAULT = "Không xác định";
    public static final String GENDER_MALE = "Nam";
    public static final String GENDER_FEMALE = "Nữ";
    public static final String CREATE = "Create";
    public static final String UPDATE = "Update";
    public static final String AVATAR = "Avatar";
    public static final String COVER = "Cover";
    public static final String TOKEN = "Token";
    public static final String DELETE_ALL = "deleteAll";
    public static final String RESTORE_ALL = "restoreAll";
}
