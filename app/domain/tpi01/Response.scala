/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package domain.tpi01

import play.api.libs.json._

case class Response(getReimbursementClaimsResponse: GetReimbursementClaimsResponse)

object Response {
  implicit val format: OFormat[Response] = Json.format[Response]
}

case class GetReimbursementClaimsResponse(responseCommon: ResponseCommon,
                                          responseDetail: Option[ResponseDetail]){
  val mdtpError: Boolean = responseCommon
    .returnParameters.exists(_.exists(_.paramName == "POSITION"))
}

object GetReimbursementClaimsResponse {
  implicit val format: OFormat[GetReimbursementClaimsResponse] = Json.format[GetReimbursementClaimsResponse]
}

case class ReturnParameter(paramName: String, paramValue: String)

object ReturnParameter {
  implicit val format: OFormat[ReturnParameter] = Json.format[ReturnParameter]
}

case class ResponseCommon(status: String,
                          processingDateTime: String,
                          correlationId: Option[String],
                          errorMessage: Option[String],
                          returnParameters: Option[List[ReturnParameter]])

object ResponseCommon {
  implicit val format: OFormat[ResponseCommon] = Json.format[ResponseCommon]
}

case class ResponseDetail(CDFPayClaimsFound: Boolean,
                          CDFPayCases: Option[List[CDFPayCase]])

object ResponseDetail {
  implicit val format: OFormat[ResponseDetail] = Json.format[ResponseDetail]
}

case class CDFPayCase(CDFPayCase: CDFPayCaseDetail)

object CDFPayCase {
  implicit val format: OFormat[CDFPayCase] = Json.format[CDFPayCase]
}


case class CDFPayCaseDetail(CDFPayCaseNumber: String,
                      CDFPayService: String,
                      caseStatus: String,
                      declarantEORI: String,
                      importerEORI: String,
                      claimantEORI: Option[String],
                      claimAmountTotal: Option[String],
                      totalCaseReimburseAmnt: Option[String]) {

  private def transformedCaseStatus: String =
    caseStatus match {
      case "Open" => "In Progress"
      case "Open-Analysis" => "In Progress"
      case "Pending-Approval" =>  "Pending"
      case "Pending-Queried" => "Pending"
      case "Resolved-Withdrawn" => "Closed"
      case "Rejected-Failed Validation" => "Closed"
      case "Resolved-Rejected" => "Closed"
      case "Open-Rework" => "In Progress"
      case "Paused" => "In Progress"
      case "Resolved-No Reply" => "Closed"
      case "RTBH-Sent" => "Closed"
      case "Resolved-Refused" => "Closed"
      case "Pending Payment Confirmation" => "Pending"
      case "Resolved-Approved" => "Closed"
      case "Resolved-Partial Refused" => "Closed"
      case "Pending Decision Letter" => "Pending"
      case "Approved" => "Closed"
      case "Analysis-Rework" => "In Progress"
      case "Rework-Payment Details" => "In Progress"
      case "Reply To RTBH" => "Pending"
      case "Pending-Compliance Recommendation" => "Pending"
      case "Pending-Compliance Check Query" => "Pending"
      case "Pending-Compliance Check" => "Pending"
    }

  def toCDSPayCaseDetail: CDFPayCaseDetail = this.copy(caseStatus = transformedCaseStatus)
}

object CDFPayCaseDetail {
  implicit val format: OFormat[CDFPayCaseDetail] = Json.format[CDFPayCaseDetail]
}


case class ErrorResponse(errorDetail: ErrorDetail)

object ErrorResponse {
  implicit val format: OFormat[ErrorResponse] = Json.format[ErrorResponse]
}

case class ErrorDetail(timestamp: String,
                       correlationId: String,
                       errorCode: String,
                       errorMessage: String,
                       source: String,
                       sourceFaultDetail: SourceFaultDetail)

object ErrorDetail {
  implicit val format: OFormat[ErrorDetail] = Json.format[ErrorDetail]
}

case class SourceFaultDetail(detail: Array[String])

object SourceFaultDetail {
  implicit val format: OFormat[SourceFaultDetail] = Json.format[SourceFaultDetail]
}
