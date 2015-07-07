package no.mesan.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import no.mesan.akka.actors.ImageFinder;
import no.mesan.akka.linkFinder.LinkFinder;
import no.mesan.akka.summary.SummaryFinder;

public class WikipediaParserMaster extends AbstractActor {

    public WikipediaParserMaster() {
        receive(ReceiveBuilder
                        .match(WikipediaScanRequest.class, this::handleScanRequest)
                        .matchAny(this::unhandled).build()
        );
    }

    private void handleScanRequest(final WikipediaScanRequest wikipediaScanRequest) {
        System.out.println("Starter scan av url: " + wikipediaScanRequest.getUrl());
        final ActorRef self = context().self();
        context().actorOf(Props.create(ImageFinder.class)).tell(wikipediaScanRequest, self);
        context().actorOf(Props.create(LinkFinder.class)).tell(wikipediaScanRequest, self);
        context().actorOf(Props.create(SummaryFinder.class)).tell(wikipediaScanRequest.getUrl(), self);
    }
}
