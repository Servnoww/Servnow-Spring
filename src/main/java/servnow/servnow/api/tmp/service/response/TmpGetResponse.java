package servnow.servnow.api.tmp.service.response;

public record TmpGetResponse(
    String tmp
) {

  public static TmpGetResponse of(String tmp) {
    return new TmpGetResponse(tmp);
  }
}