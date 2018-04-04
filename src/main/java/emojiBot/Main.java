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
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends Application {

	CJDA bot;

	public void start(Stage primaryStage){
		boolean fail = false;

		try {
			bot = new CJDABuilder(AccountType.CLIENT).setToken(getToken()).buildBlocking();

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

						if(Character.isLetter(i)){
							newMessage.append(" :regional_indicator_" + i + ": ");

						}else if(i == ' '){
							newMessage.append(" :black_large_square: ");
						}

					}

					event.getTextChannel().sendMessage(newMessage.toString()).complete();
					event.getMessage().delete().complete();

				}
			}
		});

		Label text = new Label(fail?"Bot failed":"Bot working");
		text.setScaleX(3);
		text.setScaleY(3);
		if(fail) text.setTextFill(Color.RED);

		primaryStage.setScene(new Scene(new StackPane(text), 400, 300));

		primaryStage.setOnCloseRequest(e -> {
			bot.shutdownNow();
			System.exit(0);
		});

		primaryStage.show();

	}

	static public void main(String args[]){
		launch(args);
	}

}
