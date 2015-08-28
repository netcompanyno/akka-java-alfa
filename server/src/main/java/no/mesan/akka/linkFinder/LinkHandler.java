package no.mesan.akka.linkFinder;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import no.mesan.akka.WikipediaScanRequest;

public class LinkHandler extends AbstractActor {
    public static int handledLinks;

    public LinkHandler() {
        receive(
                ReceiveBuilder
                        .match(Link.class, this::handleLink)
                        .matchAny(this::unhandled).build()
        );
    }

    private void handleLink(final Link foundLink) {
        WikipediaScanRequest request = new WikipediaScanRequest(foundLink.getUrl(), self());
        LinkHandler.handledLinks++;
        if (handledLinks < 100) {
            sender().tell(request, ActorRef.noSender());
        }
    }
}
