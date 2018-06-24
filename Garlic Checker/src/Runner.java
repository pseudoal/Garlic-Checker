import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;

public class Runner {

	private static Garlic grlc;
	private static int timer; // Seconds
	private static String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";

	private static double prevVal = 0;

	public static void main(String[] args) {

		timer = Integer.parseInt(args[0]);
		Runnable runnable = new Runnable() {

			@SuppressWarnings("static-access")
			public void run() {
				while (true) {
					String json = "";
					try {
						json = grlc.readJsonFromUrl("https://api.coinmarketcap.com/v1/ticker/garlicoin/");
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					String parsed = json.split("price_usd\": \"")[1];
					String finalValue = parsed.split("\",")[0];
					double currentVal = Double.parseDouble(finalValue);

					timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

					if (prevVal == 0) {
						System.out.println("[" + timeStamp + "]	" + "$" + currentVal);
					} else {
						if (currentVal > prevVal) {
							System.out.println("[" + timeStamp + "]	" + ANSI_GREEN + "$" + currentVal + ANSI_RESET);
							//System.out.println("[" + timeStamp + "]	" + "$" + currentVal);
						}
						if (currentVal < prevVal) {
							System.out.println("[" + timeStamp + "]	" + ANSI_RED + "$" + currentVal + ANSI_RESET);
							//System.out.println("[" + timeStamp + "]	" + "$" + currentVal);
						} else {
							System.out.println("[" + timeStamp + "]	" + "$" + currentVal);
						}
					}

					prevVal = currentVal;

					try {
						Thread.sleep(timer*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread thread = new Thread(runnable);
		thread.start();
	}

}
