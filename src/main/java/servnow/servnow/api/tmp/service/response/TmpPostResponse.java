package servnow.servnow.api.tmp.service.response;

public record TmpPostResponse(
    String tmp
) {

  public static TmpPostResponse of(String tmp) {
    return new TmpPostResponse(tmp);
  }
}