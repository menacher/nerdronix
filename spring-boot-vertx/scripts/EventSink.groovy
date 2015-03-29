package events

def eb = vertx.eventBus
eb.registerHandler("hello.listeners") { message -> println "I received a message ${message.body}" }