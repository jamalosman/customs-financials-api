/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package services

import connectors.Sub09Connector
import domain.sub09._
import models.EORI
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers.running
import play.api.{Application, inject}
import uk.gov.hmrc.http.HeaderCarrier
import utils.SpecBase

import scala.concurrent.{ExecutionContext, Future}

class SubscriptionServiceSpec extends SpecBase {

  "SubscriptionService" when {

    "calling Sub09 get subscriptions" should {
      "get SubscriptionDisplayResponse" in new Setup {
        val responseCommon: ResponseCommon = ResponseCommon("OK", None, "2020-10-05T09:30:47Z", None)
        val cdsEstablishmentAddress: CdsEstablishmentAddress = CdsEstablishmentAddress("1 street", "Southampton", Some("SO1 1AA"), "GB")
        val responseDetail: ResponseDetail = ResponseDetail(Some(eori), None, None, "CDSFullName",
          cdsEstablishmentAddress, None, None, None,
          None, None, None, None, None, None, None, ETMP_Master_Indicator = true)
        val subscriptionResponse: SubscriptionResponse = SubscriptionResponse(SubscriptionDisplayResponse(responseCommon, responseDetail))
        when(mockSub09Connector.getSubscriptions(EORI("Trader EORI"), None)).thenReturn(Future.successful(subscriptionResponse))

        running(app) {
          val result = await(service.getVerifiedEmail(EORI("Trader EORI")))
          result mustBe EmailVerifiedResponse(None)
        }
      }
    }
  }

  trait Setup {
    implicit val hc: HeaderCarrier = HeaderCarrier()
    implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

    val eori: EORI = EORI("testEORI")
    val mockSub09Connector: Sub09Connector = mock[Sub09Connector]

    val app: Application = GuiceApplicationBuilder().overrides(
      inject.bind[Sub09Connector].toInstance(mockSub09Connector)
    ).configure(
      "microservice.metrics.enabled" -> false,
      "metrics.enabled" -> false,
      "auditing.enabled" -> false
    ).build()

    val service: SubscriptionService = app.injector.instanceOf[SubscriptionService]
  }
}
