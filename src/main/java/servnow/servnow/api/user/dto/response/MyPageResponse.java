    package servnow.servnow.api.user.dto.response;

    import servnow.servnow.domain.user.model.User;
    import servnow.servnow.domain.user.model.UserInfo;
    import servnow.servnow.domain.user.model.enums.Level;
    import servnow.servnow.domain.user.model.enums.Platform;

    public record MyPageResponse(
            String nickname,
            String profileUrl,
            int point,
            Platform platform,
            int userCreatedSurveyCount,
            int userParticipatedSurveyCount
    ) {

        public static MyPageResponse of(UserInfo userInfo, User user) {
            return new MyPageResponse(userInfo.getNickname(), userInfo.getProfile_url(), userInfo.getPoint(), user.getPlatform(),
                    user.getSurveys().size(), user.getSurveyResults().size());
        }

    }
