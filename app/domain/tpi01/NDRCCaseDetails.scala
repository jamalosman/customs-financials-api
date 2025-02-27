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

import play.api.libs.json.{Json, OFormat}

case class NDRCCaseDetails(CDFPayCaseNumber: String,
                           declarationID: Option[String],
                           claimStartDate: String,
                           closedDate: Option[String],
                           caseStatus: String,
                           caseSubStatus: Option[String],
                           declarantEORI: String,
                           importerEORI: String,
                           claimantEORI: Option[String],
                           totalCustomsClaimAmount: Option[String],
                           totalVATClaimAmount: Option[String],
                           totalExciseClaimAmount: Option[String],
                           declarantReferenceNumber: Option[String],
                           basisOfClaim: Option[String]) {

  private def transformedCaseStatus: String =
    caseStatus match {
      case "Open" => "In Progress"
      case "Open-Analysis" => "In Progress"
      case "Pending-Approval" => "In Progress"
      case "Pending-Queried" => "Pending"
      case "Resolved-Withdrawn" => "Closed"
      case "Rejected-Failed Validation" => "Closed"
      case "Resolved-Rejected" => "Closed"
      case "Open-Rework" => "In Progress"
      case "Paused" => "In Progress"
      case "Resolved-No Reply" => "Closed"
      case "Resolved-Refused" => "Closed"
      case "Pending Payment Confirmation" => "In Progress"
      case "Resolved-Approved" => "Closed"
      case "Resolved-Partial Refused" => "Closed"
      case "Pending Decision Letter" => "In Progress"
      case "Approved" => "In Progress"
      case "Analysis-Rework" => "In Progress"
      case "Rework-Payment Details" => "In Progress"
      case "Pending-RTBH" => "In Progress"
      case "RTBH Sent" => "Pending"
      case "Reply To RTBH" => "Pending"
      case "Pending-Compliance Recommendation" => "In Progress"
      case "Pending-Compliance Check Query" => "Pending"
      case "Pending-Compliance Check" => "In Progress"
    }

  def toNDRCCaseDetails: NDRCCaseDetails = this.copy(caseSubStatus = Option(caseStatus),caseStatus = transformedCaseStatus)
}

object NDRCCaseDetails {
  implicit val format: OFormat[NDRCCaseDetails] = Json.format[NDRCCaseDetails]
}