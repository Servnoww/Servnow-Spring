package servnow.servnow.api.result.dto.request;

import java.util.List;

public record SurveyResultMemosPatchRequest (
  List<SurveyResultMemoPatch> memos
){
  public record SurveyResultMemoPatch(
      long id,
      String content
  ){}
}