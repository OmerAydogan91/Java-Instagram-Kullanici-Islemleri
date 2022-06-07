import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.sound.midi.Soundbank;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.pattern.FullLocationPatternConverter;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.bytedeco.javacpp.RealSense.intrinsics;

import com.omeraydogan.FollowerClass;
import com.omeraydogan.Following;

public class MainClass {

	public static void main(String[] args) {

		// 1- biografi bilgisini g�ster
		// 2- takip�i say�n� g�ster
		// 3- profil resminin linklini versin
		// 4- takip�i listesini getirsin
		// 5- takip etti�im ki�ileri getir
		// 6- uygulamy� sonland�r ��k��

		Scanner veriScanner = new Scanner(System.in);

		String username = null;
		String password = null;
		String islemler = " 1- biografi bilgisini g�ster\r\n" + "		 2- takip�i say�n� g�ster\r\n"
				+ "		 3- profil resminin linklini versin\r\n" + "		 4- takip�i listesini getirsin\r\n"
				+ "		 5- takip etti�im ki�ileri getir\r\n" + "		 6- uygulamy� sonland�r ��k�� \r\n"
				+ "��lem se�iniz: ";

		System.out.println("Instagram Projemize Ho�gelginiz...");

		System.out.print("Kullan�c� ad�n�z� giriniz:");
		username = veriScanner.nextLine();

		System.out.print("�ifreniniz giriniz:");
		password = veriScanner.nextLine();

		if (username.equals("omerjava") && password.equals("OmerJava+-*")) {
			// giri� bilgileri do�ru
			System.out.println("Kullan�c� ad�n�z ve �ifreniz do�rudur...");
			Instagram4j instagram = Instagram4j.builder().username(username).password(password).build();
			instagram.setup();

			try {

				// instagrama giri� yapt�k ve kullan�c�ya ail bilgileri eri�iyoruz...
				instagram.login();
				InstagramSearchUsernameResult userResult = instagram
						.sendRequest(new InstagramSearchUsernameRequest(username));

				System.out.println("Biografi : " + userResult.getUser().biography);
				// instagram �zerinden biografisine eri�tik

				System.out.print(islemler);
				String secim = veriScanner.nextLine();

				if (secim.equals("6")) {
					System.out.println("uygulama sonland�r�lm��t�r...");
				} else if (secim.equals("1")) {
					System.out.println("Biografi: " + userResult.getUser().biography);
				} else if (secim.equals("2")) {
					System.out.println("Takip�i Say�s�: " + userResult.getUser().follower_count); //
					System.out.println("Takip ettiklerimin Say�s�: " + userResult.getUser().following_count);
					// System.out.println("Takip�i �ehirleri: "+userResult.getUser().city_name);
					// System.out.println("Takip�i Asresleri:
					// "+userResult.getUser().address_street);
				} else if (secim.equals("3")) {
					// profil resminin linkini alma
					System.out.println("Profil resmimin linki: " + userResult.getUser().profile_pic_url);

				} else if (secim.equals("4")) {
					String takipciString = "1-mail g�nder \n2-dosyaya yazd� \n3-console yazd�r \n4-hi�bir�ey yapma";
					System.out.println(takipciString);
					String takipcisecim = veriScanner.nextLine();

					int sayac = 1;

					// takip�i listesini getirsin
					InstagramGetUserFollowersResult followerList = instagram
							.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));

					if (takipcisecim.equals("1")) {
						// mail g�nder
						int i = 1;
						StringBuffer buffer = new StringBuffer();
						for (InstagramUserSummary follower : followerList.getUsers()) {

							buffer.append(i + ". " + follower.getPk() + " " + follower.getUsername() + " "
									+ follower.getFull_name() + "\n");
							i++;

						}
						MailGonder("omeraydoganjava@gmail.com", buffer.toString());

					}

					else if (takipcisecim.equals("2")) {
						// dosyaya yazd�r
						// List<InstagramUserSummary> users = followerList.getUsers();

						List<FollowerClass> fwArraylist = new ArrayList<>();

						for (InstagramUserSummary fw : followerList.getUsers()) {
							// ful name and username and pk degerini alaca��z

							FollowerClass fwsinif = new FollowerClass();

							fwsinif.setPk(fw.getPk());// primery key d�eri

							fwsinif.setUsername(fw.getUsername());// username

							fwsinif.setFulname(fw.getFull_name());// full name

							fwArraylist.add(fwsinif);

						}

						File file = new File("C:\\Users\\MrCode\\Java Projelerim\\instagramFollewer\\follower.bin");

						if (!file.exists()) {

							file.createNewFile();

						}

						DosyayaYazdir(file, fwArraylist);

					} else if (takipcisecim.equals("3")) {
						// console yazd�r
						int i = 1;
						for (InstagramUserSummary follower : followerList.getUsers()) {
							System.out.println(i + ". " + follower.getPk() + " " + follower.getUsername() + " "
									+ follower.getFull_name());
							i++;
						}

					} else if (takipcisecim.equals("4")) {
						// hi�bir�ey yapma
						System.out.println("yap�lacak i�leminiz bulunmamaktad�r. i�lem yapmak i�in tekrar deneyiniz.");
					} else {
						System.out.println("l�tfen 1 ile 4 ars�nda bir se�im yap�n�z...");
					}
					for (InstagramUserSummary follower : followerList.getUsers()) {// takipcilerimin fulnameini d�n�yor
						System.out.println(sayac + ". :" + follower.full_name);
						sayac++;

					}
				}

				else if (secim.equals("5")) {
					// takip etti�im ki�ileri getir

					InstagramGetUserFollowersRequest followingResult = Instagram
							.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));

					String takipciIslemler = "1-mail g�nder " + "\n2-dosyaya yazd� " + "\n3-console yazd�r "
							+ "\n4-hi�bir�ey yapma";
					System.out.println(takipciIslemler);
					String takipettiklerimsecim = veriScanner.nextLine();
					InstagramGetUserFollowersResult followerList;
					if (takipettiklerimsecim.equals("1")) {
						// mail g�nder
						int i = 1;
						StringBuffer buffer = new StringBuffer();

						for (InstagramUserSummary following : followerList.getUsers()) {
							buffer.append(
									i + " ." + following.pk + " " + following.username + " " + following.full_name);
							i++;

						}
						MailGonder("omeraydoganjava@gmail.com", buffer.toString());// mail g�nderdik
					} else if (takipettiklerimsecim.equals("2")) {

						// dosya yazd�rma

						List<Following> followings = new ArrayList<Following>();
						for (InstagramUserSummary fw : followingList.getUser()) {
							Following following = new Following(fw.getPk(), fw.getUsername(), fw.getFull_name());
							
							followings.add(following);
							
						}
						File file=new File("C:\\Users\\MrCode\\Java Projelerim\\instagramFollewer\bin");
						if (!file.exists()) {
							file.createNewFile();
							
						}
						else {
							WriteFollowingToFile(file, followings);	
						}
						

					} else if (takipettiklerimsecim.equals("3")) {

						int i=1;
						for (InstagramUserSummary following :followerList.getUsers()) {
							System.out.println(i + ". " + following.getPk() + " " + following.getUsername() + " "
									+ following.getFull_name());
							i++;
						}
						
						// console yazd�r
					} else if (takipettiklerimsecim.equals("4")) {
						// hi�bir�ey yapma
						System.out.println("se�im yap�lmad� i�lem yap�lmaz");
					} else {
						System.out.println("Hatal� giri� yapt�n�z l�tfeen 1 ile 4 aras�nda de�er giri�i yap�n�z...");
					}
				}

			} catch (ClientProtocolException e) {
				System.out.println("Error : " + e.getMessage());

			} catch (IOException e) {
				System.out.println("Error : " + e.getMessage());
			}

		} else {
			System.out.println("kullan�c� ad�n�z veya �ifreniz yanl��t�r...");
		}

	}

	public static void DosyayaYazdir(File file, List<FollowerClass> followers) // dosyayazd�r ad�nda metot
																				// tan�ml�yoruz...
	{
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
			out.writeObject(followers);
			System.out.println("takip�i listesi ba�ar�l� bir �ekilde dosyaya yazd�r�ld�...");
		} catch (FileNotFoundException e) {
			System.out.println("hata : " + e.getMessage());
		} catch (IOException e) {
			System.out.println("hata : " + e.getMessage());
		}

	}

	public static void MailGonder(String to, String icerik) {
		String fromEmail = "omeraydoganjava@gmail.com";
		String fromPassword = "Java3427+-*";

		Properties properties = new Properties();

		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		properties.put("mail.smtp.starttls.enable", true);
		properties.put("mail.smtp.port", "587");

		Session sesesion = Session.getInstance(properties, new Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return null; // new PasswordAuthentication(fromEmail, fromPassword);
			}
		});

		try {
			Message message = new MimeMessage(sesesion);

			message.setFrom(new InternetAdress(fromEmail));

			message.setRecipient(RecipientType.TO, InternetAdress.parse(to));

			message.setSubject("Takipci listesi");

			message.setText(icerik);

			Transport.send(message);
			System.out.println("mail ba�ar�l� bir �ekide g�nderildi...");

		} catch (Exception e) {
			System.out.println("mail g�nderirken hata olu�tu: " + e);
		}
	}

	public static void WriteFollowingToFile(File file, List<Following> following) // dosyayazd�r ad�nda metot
	// tan�ml�yoruz...
	{
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
			out.writeObject(following);
			System.out.println("takip�i listesi ba�ar�l� bir �ekilde dosyaya yazd�r�ld�...");
		} catch (FileNotFoundException e) {
			System.out.println("hata : " + e.getMessage());
		} catch (IOException e) {
			System.out.println("hata : " + e.getMessage());
		}

	}

}
