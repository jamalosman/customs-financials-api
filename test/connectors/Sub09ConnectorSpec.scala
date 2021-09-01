/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package connectors

import domain.sub09._
import models.EORI
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}
import utils.SpecBase

import scala.concurrent.Future

class Sub09ConnectorSpec extends SpecBase {

  "getSubscriptions" should {
    "return a json on a successful response" in new Setup {
      when[Future[SubscriptionResponse]](mockHttpClient.GET(any, any, any)(any, any, any))
        .thenReturn(Future.successful(response))

      running(app) {
        val result = await(connector.getSubscriptions(EORI("someEori"), hc.requestId))
        result mustBe response
      }
    }
  }

  trait Setup {
    implicit val hc: HeaderCarrier = HeaderCarrier()
    val mockHttpClient: HttpClient = mock[HttpClient]
    val responseCommon: ResponseCommon = ResponseCommon("OK", None, "2020-10-05T09:30:47Z", None)
    val cdsEstablishmentAddress: CdsEstablishmentAddress = CdsEstablishmentAddress("Example Street", "Example", Some("A00 0AA"), "GB")
    val responseDetail: ResponseDetail = ResponseDetail(Some(EORI("someEori")), None, None, "CDSFullName",
      cdsEstablishmentAddress, None, None, None,
      None, None, None, None, None, None, None, ETMP_Master_Indicator = true)
    val response: SubscriptionResponse = SubscriptionResponse(SubscriptionDisplayResponse(responseCommon, responseDetail))

    val app: Application = GuiceApplicationBuilder().overrides(
      bind[HttpClient].toInstance(mockHttpClient)
    ).configure(
      "microservice.metrics.enabled" -> false,
      "metrics.enabled" -> false,
      "auditing.enabled" -> false
    ).build()

    val connector: Sub09Connector = app.injector.instanceOf[Sub09Connector]
  }

}
