package emojiBot;

import bots.JDAAddon.CJDA;
import static bots.JDAAddon.CJDABuilder.*;
import bots.JDAAddon.CJDABuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends Application {

	CJDA bot;
	String Token = getToken();

	public void start(Stage primaryStage){
		System.out.println(Token);

		Label text = new Label("Bot setting up");
		text.setScaleX(3);
		text.setScaleY(3);

		primaryStage.setScene(new Scene(new StackPane(text), 400, 300));

		primaryStage.setOnCloseRequest(e -> {
			bot.shutdownNow();
			System.exit(0);
		});

		primaryStage.show();
		boolean fail = false;

		try {
			while(bot == null) {
				bot = new CJDABuilder(AccountType.CLIENT).setToken(Token).buildBlocking();
				bot.setAutoReconnect(true);
			}

		}catch(Exception e){
			e.printStackTrace();
			fail = true;
		}

		bot.addEventListener(new ListenerAdapter() {
			@Override
			public void onMessageReceived(MessageReceivedEvent event) {
				if(event.getAuthor().equals(bot.getSelfUser()) &&
						!event.getMessage().getContent().startsWith(":")){

					StringBuilder newMessage = new StringBuilder();

					for(char i: event.getMessage().getContent().toCharArray()){

						if(('a' <= i && i <= 'z') || ('A' <= i && i <= 'Z')){
							newMessage.append(" :regional_indicator_" + Character.toLowerCase(i) + ": ");

						}else if(i == ' '){
							newMessage.append(" :black_large_square: ");
						}

					}

					event.getChannel().sendMessage(newMessage.toString()).complete();
					event.getMessage().delete().complete();

				}
			}

			@Override
			public void onDisconnect(DisconnectEvent event) {
				super.onDisconnect(event);
				text.setText("Bot disconnected");
				text.setTextFill(Color.RED);
			}

			@Override
			public void onReconnect(ReconnectedEvent event) {
				super.onReconnect(event);
				text.setText("Bot reconnected");
				text.setTextFill(Color.BLACK);
			}
		});


		text.setText(fail?"Bot failed":"Bot working");
		if(fail) text.setTextFill(Color.RED);

	}

	static public void main(String args[]){
		launch(args);
	}

}
