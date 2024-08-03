    package servnow.servnow.api.user.controller.response;

    import servnow.servnow.domain.user.model.User;
    import servnow.servnow.domain.user.model.UserInfo;
    import servnow.servnow.domain.user.model.enums.Level;

    public record MyPageResponse(
            String nickname,
            String profileUrl,
            int point,
            Level level,
            int userCreatedSurveyCount,
            int userParticipatedSurveyCount
    ) {

        public static MyPageResponse of(UserInfo userInfo, User user) {
            return new MyPageResponse(userInfo.getNickname(), userInfo.getProfile_url(), userInfo.getPoint(), userInfo.getLevel(),
                    user.getSurveys().size(), user.getSurveyResults().size());
        }

    }
