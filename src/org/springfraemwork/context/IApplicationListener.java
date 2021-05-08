package org.springfraemwork.context;

import org.springfraemwork.context.event.ContextClosedEvent;

public interface IApplicationListener<C> {
     void onApplicationEvent(ContextClosedEvent e);
}
