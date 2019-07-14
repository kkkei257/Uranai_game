import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

class Uranai{
	public static final int MIKUJI_MAIN = 1;
	public static final int MIKUJI_START = 2;
	public static final int MIKUJI_PLAY = 3;
	public static final int MIKUJI_PLAY_KEKKA = 4;
	public static final int MIKUJI_EXIT = 5;

	public static final int SEIZA_MAIN = 21;
	public static final int SEIZA_START = 22;
	public static final int SEIZA_PLAY = 23;
	public static final int SEIZA_PLAY_KEKKA = 24;
	public static final int SEIZA_EXIT = 25;

	public static final int HL_MAIN = 41;
	public static final int HL_START = 42;
	public static final int HL_SELECT = 43;
	public static final int HL_PLAY = 44;
	public static final int HL_EXIT = 45;

	public static final int SLOT_MAIN = 61;
	public static final int SLOT_START = 62;
	public static final int SLOT_PLAY_SELECT1 = 63;
	public static final int SLOT_PLAY_SELECT2 = 64;
	public static final int SLOT_PLAY_POINT = 65;
	public static final int SLOT_PLAY = 66;
	public static final int SLOT_EXIT = 67;

	public static final int SETTING = 99;

	int gamestate = HL_MAIN;

	public static final int w = 1366;//このwとhはPCの解像度の値。
	public static final int h = 768;//ゲームを実行するPCに合わせて値を変更すべし。

	JFrame frame;
	BufferStrategy bs;
	Timer t;
	Random rnd = new Random();
	Image mikuji_main,seiza_main,hl_main,slot_main,
	      mikuji_num,
	      mikuji_daikiti,mikuji_kiti,mikuji_chuukiti,mikuji_shoukiti,
	      mikuji_suekiti,mikuji_kyou,
	      seiza_haikei,seiza_hituji,seiza_hituji_sel,seiza_oushi,seiza_oushi_sel,
	      seiza_hutago,seiza_hutago_sel,seiza_kani,seiza_kani_sel,
	      seiza_sisi,seiza_sisi_sel,seiza_otome,seiza_otome_sel,
	      seiza_tenbin,seiza_tenbin_sel,seiza_sasori,seiza_sasori_sel,
	      seiza_ite,seiza_ite_sel,seiza_yagi,seiza_yagi_sel,
	      seiza_mizugame,seiza_mizugame_sel,seiza_uo,seiza_uo_sel,
	      trump_ura,selected,high,low,slot_select_3,
	      slot_select_5,slot_select_10,slot_easy,slot_normal,slot_hard,slot_point,
	      trump_haikei,hl_haikei2,hl_select3,hl_select5,hl_select10,hl_select_back,
	      yazirusi_left,yazirusi_right,mikuji_kami,
	      blue,blue_sel,red,red_sel,yellow,yellow_sel,green,green_sel;
	Image[] trump = new Image[52];
	Image[] slot = new Image[11];
	Image[] hoshi = new Image[4];
	Image[] seiza_kekka = new Image[24];

	AudioClip bgm=null,se_button=null,se_slot=null,se_card=null,se_mikuji=null;

	String nums = "",slot_points,slot_pointn_sums;
	/*String[] lucky_color = {"ホワイト","ブラック","レッド","グリーン","イエロー","ブルー",
			"ピンク","ブラウン","パープル","オレンジ","シルバー","ゴールド","ライトブルー"};//0～12
	String[] lucky_item = {"","","","","","","","","","","","","","",""};*/

	int waittimer,setting_in,
	    num = 0,ran = 0,ran_before = 0,ran1,ran2,ran3,
		hl_play,select,states,count,enter_state,hl_state,count_state,hl_win,
		win_state,slot_level,slot_state,slot_play,slot_reel,a1,a2,a3,//a1,a2,a3は1,2,3を押したかどうかの制御用。
	    slot_string,slot_pointn,reel_1,reel_2,reel_3,slot_pointn_sum,
	    setting_state;
	int[] choiced = new int[52];
	float yl=0,yr=0;

	static int hl_3_1=0,hl_3_2=0,hl_3_3=0;
	static int hl_5_1=0,hl_5_2=0,hl_5_3=0,hl_5_4=0;
	static int hl_10_1=0,hl_10_2=0,hl_10_3=0,hl_10_4=0,hl_10_5=0;
	static int slot_pointn_max=0,seiza_day=0;
	static int player=0,mikuji_player=0,seiza_player=0,hl_player=0,slot_player=0;

	Uranai(int w,int h,String title){
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.setLayout(null);
        frame.setSize(w,h);
		frame.setLocationRelativeTo(null);

		frame.setIgnoreRepaint(true);
		frame.createBufferStrategy(2);
		bs = frame.getBufferStrategy();

		String ipos = "images/";
		try{
			mikuji_main = ImageIO.read(getClass().getResource(ipos+"mikuji_main.png"));
			seiza_main = ImageIO.read(getClass().getResource(ipos+"seiza_main.png"));
			hl_main = ImageIO.read(getClass().getResource(ipos+"hl_main.png"));
			slot_main = ImageIO.read(getClass().getResource(ipos+"slot_main.png"));

			mikuji_num = ImageIO.read(getClass().getResource(ipos+"mikuji_num.png"));
			mikuji_daikiti = ImageIO.read(getClass().getResource(ipos+"mikuji_daikiti.png"));
			mikuji_kiti = ImageIO.read(getClass().getResource(ipos+"mikuji_kiti.png"));
			mikuji_chuukiti = ImageIO.read(getClass().getResource(ipos+"mikuji_chuukiti.png"));
			mikuji_shoukiti = ImageIO.read(getClass().getResource(ipos+"mikuji_shoukiti.png"));
			mikuji_suekiti = ImageIO.read(getClass().getResource(ipos+"mikuji_suekiti.png"));
			mikuji_kyou = ImageIO.read(getClass().getResource(ipos+"mikuji_kyou.png"));

			seiza_haikei = ImageIO.read(getClass().getResource(ipos+"seiza_haikei.png"));
			seiza_hituji = ImageIO.read(getClass().getResource(ipos+"seiza_icon_hituji.png"));
			seiza_hituji_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_hituji_select.png"));
			seiza_oushi = ImageIO.read(getClass().getResource(ipos+"seiza_icon_oushi.png"));
			seiza_oushi_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_oushi_select.png"));
			seiza_hutago = ImageIO.read(getClass().getResource(ipos+"seiza_icon_hutago.png"));
			seiza_hutago_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_hutago_select.png"));
			seiza_kani = ImageIO.read(getClass().getResource(ipos+"seiza_icon_kani.png"));
			seiza_kani_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_kani_select.png"));
			seiza_sisi = ImageIO.read(getClass().getResource(ipos+"seiza_icon_sisi.png"));
			seiza_sisi_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_sisi_select.png"));
			seiza_otome = ImageIO.read(getClass().getResource(ipos+"seiza_icon_otome.png"));
			seiza_otome_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_otome_select.png"));
			seiza_tenbin = ImageIO.read(getClass().getResource(ipos+"seiza_icon_tenbin.png"));
			seiza_tenbin_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_tenbin_select.png"));
			seiza_sasori = ImageIO.read(getClass().getResource(ipos+"seiza_icon_sasori.png"));
			seiza_sasori_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_sasori_select.png"));
			seiza_ite = ImageIO.read(getClass().getResource(ipos+"seiza_icon_ite.png"));
			seiza_ite_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_ite_select.png"));
			seiza_yagi = ImageIO.read(getClass().getResource(ipos+"seiza_icon_yagi.png"));
			seiza_yagi_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_yagi_select.png"));
			seiza_mizugame = ImageIO.read(getClass().getResource(ipos+"seiza_icon_mizugame.png"));
			seiza_mizugame_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_mizugame_select.png"));
			seiza_uo = ImageIO.read(getClass().getResource(ipos+"seiza_icon_uo.png"));
			seiza_uo_sel = ImageIO.read(getClass().getResource(ipos+"seiza_icon_uo_select.png"));

			trump[0] = ImageIO.read(getClass().getResource(ipos+"c01.png"));
			trump[1] = ImageIO.read(getClass().getResource(ipos+"d01.png"));
			trump[2] = ImageIO.read(getClass().getResource(ipos+"h01.png"));
			trump[3] = ImageIO.read(getClass().getResource(ipos+"s01.png"));
			trump[4] = ImageIO.read(getClass().getResource(ipos+"c02.png"));
			trump[5] = ImageIO.read(getClass().getResource(ipos+"d02.png"));
			trump[6] = ImageIO.read(getClass().getResource(ipos+"h02.png"));
			trump[7] = ImageIO.read(getClass().getResource(ipos+"s02.png"));
			trump[8] = ImageIO.read(getClass().getResource(ipos+"c03.png"));
			trump[9] = ImageIO.read(getClass().getResource(ipos+"d03.png"));
			trump[10] = ImageIO.read(getClass().getResource(ipos+"h03.png"));
			trump[11] = ImageIO.read(getClass().getResource(ipos+"s03.png"));
			trump[12] = ImageIO.read(getClass().getResource(ipos+"c04.png"));
			trump[13] = ImageIO.read(getClass().getResource(ipos+"d04.png"));
			trump[14] = ImageIO.read(getClass().getResource(ipos+"h04.png"));
			trump[15] = ImageIO.read(getClass().getResource(ipos+"s04.png"));
			trump[16] = ImageIO.read(getClass().getResource(ipos+"c05.png"));
			trump[17] = ImageIO.read(getClass().getResource(ipos+"d05.png"));
			trump[18] = ImageIO.read(getClass().getResource(ipos+"h05.png"));
			trump[19] = ImageIO.read(getClass().getResource(ipos+"s05.png"));
			trump[20] = ImageIO.read(getClass().getResource(ipos+"c06.png"));
			trump[21] = ImageIO.read(getClass().getResource(ipos+"d06.png"));
			trump[22] = ImageIO.read(getClass().getResource(ipos+"h06.png"));
			trump[23] = ImageIO.read(getClass().getResource(ipos+"s06.png"));
			trump[24] = ImageIO.read(getClass().getResource(ipos+"c07.png"));
			trump[25] = ImageIO.read(getClass().getResource(ipos+"d07.png"));
			trump[26] = ImageIO.read(getClass().getResource(ipos+"h07.png"));
			trump[27] = ImageIO.read(getClass().getResource(ipos+"s07.png"));
			trump[28] = ImageIO.read(getClass().getResource(ipos+"c08.png"));
			trump[29] = ImageIO.read(getClass().getResource(ipos+"d08.png"));
			trump[30] = ImageIO.read(getClass().getResource(ipos+"h08.png"));
			trump[31] = ImageIO.read(getClass().getResource(ipos+"s08.png"));
			trump[32] = ImageIO.read(getClass().getResource(ipos+"c09.png"));
			trump[33] = ImageIO.read(getClass().getResource(ipos+"d09.png"));
			trump[34] = ImageIO.read(getClass().getResource(ipos+"h09.png"));
			trump[35] = ImageIO.read(getClass().getResource(ipos+"s09.png"));
			trump[36] = ImageIO.read(getClass().getResource(ipos+"c10.png"));
			trump[37] = ImageIO.read(getClass().getResource(ipos+"d10.png"));
			trump[38] = ImageIO.read(getClass().getResource(ipos+"h10.png"));
			trump[39] = ImageIO.read(getClass().getResource(ipos+"s10.png"));
			trump[40] = ImageIO.read(getClass().getResource(ipos+"c11.png"));
			trump[41] = ImageIO.read(getClass().getResource(ipos+"d11.png"));
			trump[42] = ImageIO.read(getClass().getResource(ipos+"h11.png"));
			trump[43] = ImageIO.read(getClass().getResource(ipos+"s11.png"));
			trump[44] = ImageIO.read(getClass().getResource(ipos+"c12.png"));
			trump[45] = ImageIO.read(getClass().getResource(ipos+"d12.png"));
			trump[46] = ImageIO.read(getClass().getResource(ipos+"h12.png"));
			trump[47] = ImageIO.read(getClass().getResource(ipos+"s12.png"));
			trump[48] = ImageIO.read(getClass().getResource(ipos+"c13.png"));
			trump[49] = ImageIO.read(getClass().getResource(ipos+"d13.png"));
			trump[50] = ImageIO.read(getClass().getResource(ipos+"h13.png"));
			trump[51] = ImageIO.read(getClass().getResource(ipos+"s13.png"));
			trump_ura = ImageIO.read(getClass().getResource(ipos+"ura(black).png"));
			selected = ImageIO.read(getClass().getResource(ipos+"select.png"));
			high = ImageIO.read(getClass().getResource(ipos+"high.png"));
			low = ImageIO.read(getClass().getResource(ipos+"low.png"));

			slot[0] = ImageIO.read(getClass().getResource(ipos+"slot_dia.png"));
			slot[1] = ImageIO.read(getClass().getResource(ipos+"slot_1.png"));
			slot[2] = ImageIO.read(getClass().getResource(ipos+"slot_2.png"));
			slot[3] = ImageIO.read(getClass().getResource(ipos+"slot_3.png"));
			slot[4] = ImageIO.read(getClass().getResource(ipos+"slot_4.png"));
			slot[5] = ImageIO.read(getClass().getResource(ipos+"slot_5.png"));
			slot[6] = ImageIO.read(getClass().getResource(ipos+"slot_6.png"));
			slot[7] = ImageIO.read(getClass().getResource(ipos+"slot_7.png"));
			slot[8] = ImageIO.read(getClass().getResource(ipos+"slot_8.png"));
			slot[9] = ImageIO.read(getClass().getResource(ipos+"slot_9.png"));
			slot_select_3 = ImageIO.read(getClass().getResource(ipos+"slot_select_3.png"));
			slot_select_5 = ImageIO.read(getClass().getResource(ipos+"slot_select_5.png"));
			slot_select_10 = ImageIO.read(getClass().getResource(ipos+"slot_select_10.png"));
			slot_easy = ImageIO.read(getClass().getResource(ipos+"slot_easy.png"));
			slot_normal = ImageIO.read(getClass().getResource(ipos+"slot_normal.png"));
			slot_hard = ImageIO.read(getClass().getResource(ipos+"slot_hard.png"));
			slot_point = ImageIO.read(getClass().getResource(ipos+"slot_point.png"));

			trump_haikei = ImageIO.read(getClass().getResource(ipos+"hl_haikei.png"));
			hl_select3 = ImageIO.read(getClass().getResource(ipos+"hl_select3.png"));
			hl_select5 = ImageIO.read(getClass().getResource(ipos+"hl_select5.png"));
			hl_select10 = ImageIO.read(getClass().getResource(ipos+"hl_select10.png"));
			hl_select_back = ImageIO.read(getClass().getResource(ipos+"hl_select_back.png"));
			hl_haikei2 = ImageIO.read(getClass().getResource(ipos+"hl_haikei2.png"));

			yazirusi_left = ImageIO.read(getClass().getResource(ipos+"yazirusi_left.png"));
			yazirusi_right = ImageIO.read(getClass().getResource(ipos+"yazirusi_right.png"));
			mikuji_kami = ImageIO.read(getClass().getResource(ipos+"mikuji_kami.png"));

			blue = ImageIO.read(getClass().getResource(ipos+"blue.png"));
			blue_sel = ImageIO.read(getClass().getResource(ipos+"blue_sel.png"));
			red = ImageIO.read(getClass().getResource(ipos+"red.png"));
			red_sel = ImageIO.read(getClass().getResource(ipos+"red_sel.png"));
			yellow = ImageIO.read(getClass().getResource(ipos+"yellow.png"));
			yellow_sel = ImageIO.read(getClass().getResource(ipos+"yellow_sel.png"));
			green = ImageIO.read(getClass().getResource(ipos+"green.png"));
			green_sel = ImageIO.read(getClass().getResource(ipos+"green_sel.png"));
			// = ImageIO.read(getClass().getResource(".png"));
			hoshi[0] = ImageIO.read(getClass().getResource(ipos+"hoshi0.png"));
			hoshi[1] = ImageIO.read(getClass().getResource(ipos+"hoshi1.png"));
			hoshi[2] = ImageIO.read(getClass().getResource(ipos+"hoshi2.png"));
			hoshi[3] = ImageIO.read(getClass().getResource(ipos+"hoshi3.png"));

			seiza_kekka[0] = ImageIO.read(getClass().getResource(ipos+"11_1_ohitsuji.png"));
			seiza_kekka[1] = ImageIO.read(getClass().getResource(ipos+"11_1_ousi.png"));
			seiza_kekka[2] = ImageIO.read(getClass().getResource(ipos+"11_1_hutago.png"));
			seiza_kekka[3] = ImageIO.read(getClass().getResource(ipos+"11_1_kani.png"));
			seiza_kekka[4] = ImageIO.read(getClass().getResource(ipos+"11_1_sisi.png"));
			seiza_kekka[5] = ImageIO.read(getClass().getResource(ipos+"11_1_otome.png"));
			seiza_kekka[6] = ImageIO.read(getClass().getResource(ipos+"11_1_tenbin.png"));
			seiza_kekka[7] = ImageIO.read(getClass().getResource(ipos+"11_1_sasori.png"));
			seiza_kekka[8] = ImageIO.read(getClass().getResource(ipos+"11_1_ite.png"));
			seiza_kekka[9] = ImageIO.read(getClass().getResource(ipos+"11_1_yagi.png"));
			seiza_kekka[10] = ImageIO.read(getClass().getResource(ipos+"11_1_mizugame.png"));
			seiza_kekka[11] = ImageIO.read(getClass().getResource(ipos+"11_1_uo.png"));
			seiza_kekka[12] = ImageIO.read(getClass().getResource(ipos+"11_2_ohitsuji.png"));
			seiza_kekka[13] = ImageIO.read(getClass().getResource(ipos+"11_2_ousi.png"));
			seiza_kekka[14] = ImageIO.read(getClass().getResource(ipos+"11_2_hutago.png"));
			seiza_kekka[15] = ImageIO.read(getClass().getResource(ipos+"11_2_kani.png"));
			seiza_kekka[16] = ImageIO.read(getClass().getResource(ipos+"11_2_sisi.png"));
			seiza_kekka[17] = ImageIO.read(getClass().getResource(ipos+"11_2_otome.png"));
			seiza_kekka[18] = ImageIO.read(getClass().getResource(ipos+"11_2_tenbin.png"));
			seiza_kekka[19] = ImageIO.read(getClass().getResource(ipos+"11_2_sasori.png"));
			seiza_kekka[20] = ImageIO.read(getClass().getResource(ipos+"11_2_ite.png"));
			seiza_kekka[21] = ImageIO.read(getClass().getResource(ipos+"11_2_yagi.png"));
			seiza_kekka[22] = ImageIO.read(getClass().getResource(ipos+"11_2_mizugame.png"));
			seiza_kekka[23] = ImageIO.read(getClass().getResource(ipos+"11_2_uo.png"));

		}catch(Exception e){
			System.out.println("画像読み込みエラー");
		}

		try{
			BufferedReader br = new BufferedReader(new FileReader("Uranai_savedata.txt"));

			String hl_3_1s = br.readLine();
			hl_3_1 = Integer.parseInt(hl_3_1s);
			String hl_3_2s = br.readLine();
			hl_3_2 = Integer.parseInt(hl_3_2s);
			String hl_3_3s = br.readLine();
			hl_3_3 = Integer.parseInt(hl_3_3s);

			String hl_5_1s = br.readLine();
			hl_5_1 = Integer.parseInt(hl_5_1s);
			String hl_5_2s = br.readLine();
			hl_5_2 = Integer.parseInt(hl_5_2s);
			String hl_5_3s = br.readLine();
			hl_5_3 = Integer.parseInt(hl_5_3s);
			String hl_5_4s = br.readLine();
			hl_5_4 = Integer.parseInt(hl_5_4s);

			String hl_10_1s = br.readLine();
			hl_10_1 = Integer.parseInt(hl_10_1s);
			String hl_10_2s = br.readLine();
			hl_10_2 = Integer.parseInt(hl_10_2s);
			String hl_10_3s = br.readLine();
			hl_10_3 = Integer.parseInt(hl_10_3s);
			String hl_10_4s = br.readLine();
			hl_10_4 = Integer.parseInt(hl_10_4s);
			String hl_10_5s = br.readLine();
			hl_10_5 = Integer.parseInt(hl_10_5s);

			String slot_pointn_maxs = br.readLine();
			slot_pointn_max = Integer.parseInt(slot_pointn_maxs);

			String players = br.readLine();
			player = Integer.parseInt(players);

			String mikuji_players = br.readLine();
			mikuji_player = Integer.parseInt(mikuji_players);
			String seiza_players = br.readLine();
			seiza_player = Integer.parseInt(seiza_players);
			String hl_players = br.readLine();
			hl_player = Integer.parseInt(hl_players);
			String slot_players = br.readLine();
			slot_player = Integer.parseInt(slot_players);

			String seiza_days = br.readLine();
			seiza_day = Integer.parseInt(seiza_days);

			br.close();
		}catch(IOException e){
			System.out.println("入出力エラー");
		}

		//AudioClip bgm = java.applet.Applet.newAudioClip(Uranai.class.getResource("bgm.wav"));
		//bgm.loop();

		scheduler();

		/*frame.addKeyListener(new KA());

		t = new Timer();
		t.schedule(new TT(),10,30);*/
	}

	//cancel()した後にこのメソッドを起動すればまたrun()メソッドを定期実行できる。
	public void scheduler(){
		frame.addKeyListener(new KA());
		t = new Timer();
		t.schedule(new TT(),10,30);
	}

	class KA extends KeyAdapter{
		public void keyPressed(KeyEvent ev){
			int keycode = ev.getKeyCode();
		}

		public void keyReleased(KeyEvent ev){
			int keycode = ev.getKeyCode();

			if(keycode == KeyEvent.VK_ENTER) setting_in = 0;
			if(keycode == KeyEvent.VK_S){
				waittimer = 50;
				setting_in++;
				if(setting_in > 10){
					setting_state = 0;
					gamestate = SETTING;
				}
			}

			switch(gamestate){
			case MIKUJI_MAIN:
				if(keycode == KeyEvent.VK_RIGHT) gamestate=HL_MAIN;
				if(keycode == KeyEvent.VK_LEFT) gamestate=SLOT_MAIN;
				if(keycode == KeyEvent.VK_ENTER){
					select = 1;
					enter_state = 0;
					gamestate=MIKUJI_START;
				}
				break;
			case SEIZA_MAIN:
				if(keycode == KeyEvent.VK_RIGHT) gamestate=SLOT_MAIN;
				if(keycode == KeyEvent.VK_LEFT) gamestate=HL_MAIN;
				if(keycode == KeyEvent.VK_ENTER){
					select = 1;
					gamestate=SEIZA_START;
				}
				break;
			case HL_MAIN:
				if(keycode == KeyEvent.VK_RIGHT) gamestate=SLOT_MAIN;
				if(keycode == KeyEvent.VK_LEFT) gamestate=MIKUJI_MAIN;
				if(keycode == KeyEvent.VK_ENTER){
					select = 0;
					gamestate=HL_START;
				}
				break;
			case SLOT_MAIN:
				if(keycode == KeyEvent.VK_RIGHT) gamestate=MIKUJI_MAIN;
				if(keycode == KeyEvent.VK_LEFT) gamestate=HL_MAIN;
				if(keycode == KeyEvent.VK_ENTER){
					select = 0;//3回,5回,10回を選択
					slot_level = 1;//ゲームレベルを選択(easy(1),normal(2),hard(3))
					gamestate=SLOT_START;
				}
				break;

			case MIKUJI_PLAY:
				//nums = ev.getKeyText(keycode);
				if(keycode == KeyEvent.VK_BACK_SPACE){
					setting_in = 0;
					gamestate = MIKUJI_MAIN;
				}
				if(keycode == KeyEvent.VK_RIGHT){
					select++;
					if(select > 4) select = 4;
				}
				if(keycode == KeyEvent.VK_LEFT){
					select--;
					if(select < 1) select = 1;
				}
				if(keycode == KeyEvent.VK_ENTER){
						ran = rnd.nextInt(4);
						ran1 = rnd.nextInt(4);
						ran2 = rnd.nextInt(4);
						ran3 = rnd.nextInt(4);
						while(ran1 == ran) ran1 = rnd.nextInt(4);
						while(ran2 == ran1 || ran2 == ran) ran2 = rnd.nextInt(4);
						while(ran3 == ran2 || ran3 == ran1 || ran3 == ran){
							ran3 = rnd.nextInt(4);
						}

						gamestate = MIKUJI_PLAY_KEKKA;
						enter_state = 1;
						waittimer = 40;
					//ran = rnd.nextInt(6)+1; //1～6(大吉～凶)
					//if(ran )
					//gannbou = rnd.nextInt //1～
					//gamestate = MIKUJI_PLAY_KEKKA;
				}
				break;
			case MIKUJI_PLAY_KEKKA:
				if(keycode == KeyEvent.VK_ENTER){
					switch(enter_state){
					case 1:
						break;
					case 2:
						yl=0;
					    yr=0;
					    player++;
					    mikuji_player++;
					    gamestate = MIKUJI_MAIN;
						break;
					}
				}
				break;
			case SEIZA_PLAY:
				if(keycode == KeyEvent.VK_BACK_SPACE){
					setting_in = 0;
					gamestate = SEIZA_MAIN;
				}
				if(keycode == KeyEvent.VK_RIGHT){
					select++;
					if(select == 13) select = 1;
				}
				if(keycode == KeyEvent.VK_LEFT){
					select--;
					if(select == 0) select = 12;
				}
				if(keycode == KeyEvent.VK_UP){
					if(select >= 1 && select <=6) select += 6;
					else select -= 6;
				}
				if(keycode == KeyEvent.VK_DOWN){
					if(select >=7 && select <= 12) select -= 6;
					else select += 6;
				}
				if(keycode == KeyEvent.VK_ENTER) gamestate=SEIZA_PLAY_KEKKA;
				break;
			case SEIZA_PLAY_KEKKA:
				if(keycode == KeyEvent.VK_ENTER){
					yl=0;
					yr=0;
					player++;
					seiza_player++;
					gamestate = SEIZA_MAIN;
				}
				break;
			case HL_SELECT:
				if(keycode == KeyEvent.VK_BACK_SPACE){
					setting_in = 0;
					gamestate = HL_MAIN;
				}
				if(keycode == KeyEvent.VK_RIGHT){
					select++;
					if(select > 2) select = 2;
				}
				if(keycode == KeyEvent.VK_LEFT){
					select--;
					if(select < 0) select = 0;
				}
				if(keycode == KeyEvent.VK_ENTER){
					if(select == 0) hl_play = 3;
					if(select == 1) hl_play = 5;
					if(select == 2) hl_play = 10;
					for(int i=0;i<52;i++){
						choiced[i] = 0;
					}
					count = 0;//何回ループを行ったかをカウントする。
					count_state = 1;//カウントを増加させるかのチェック用
					hl_win = 0;//何回かったかを記録
					win_state = 0;//勝ったか負けたかを判別
					hl_state = 0;//0→チョイス、1→結果
					select = 1;//high,lowの選択用に使う。
					enter_state = 0;//エンターを制御(high,low選択したら1、ループ終了時は2になる)
					ran = rnd.nextInt(52);//トランプをランダムに選ぶ。
					while(choiced[ran] == 1){ //かぶりを防ごう。
						ran = rnd.nextInt(52);
					}
					choiced[ran] = 1;
					gamestate=HL_PLAY;
				}
			    break;
			case HL_PLAY:
				if(keycode == KeyEvent.VK_RIGHT){
					if(enter_state == 0) select = 2;}
				if(keycode == KeyEvent.VK_LEFT){
					if(enter_state == 0) select = 1;}
				if(keycode == KeyEvent.VK_ENTER){
					switch(enter_state){
					case 0:
						//enter_stateが0...High,Lowの選択にエンターを使う。
						//if(count_state == 1) count++;
						waittimer = 30;
						ran_before = ran;
						ran = rnd.nextInt(52);
						while(choiced[ran] == 1){
							ran = rnd.nextInt(52);
						}
						choiced[ran] = 1;
						enter_state = 1;//矢印キーが使えなくなる。
						hl_state = 1;//high,lowの選択画面時にエンターを押すと先へ進む。
						break;
					case 1:
						//enter_stateが1の時はエンターを押しても何も起きない。
						break;
					case 2:
						//5,10回終わってゲームが終了...enter_stateが2になり、エンターを押すとメイン画面へ
						yl=0;
						yr=0;
						player++;
						hl_player++;
						gamestate = HL_EXIT;
						break;
					}
				}
				break;
			case SLOT_PLAY_SELECT1:
				if(keycode == KeyEvent.VK_BACK_SPACE){
					setting_in = 0;
					gamestate = SLOT_MAIN;
				}
				if(keycode == KeyEvent.VK_RIGHT){
					select++;
					if(select > 2){
						select = 2;
					}
				}
				if(keycode == KeyEvent.VK_LEFT){
					select--;
					if(select < 0){
						select = 0;
					}
				}
				if(keycode == KeyEvent.VK_ENTER){
					if(select == 0) slot_play = 3;
					if(select == 1) slot_play = 5;
					if(select == 2) slot_play = 10;
					gamestate = SLOT_PLAY_SELECT2;
				}
				break;
			case SLOT_PLAY_SELECT2:
				if(keycode == KeyEvent.VK_BACK_SPACE){
					setting_in = 0;
					gamestate = SLOT_PLAY_SELECT1;
				}
				if(keycode == KeyEvent.VK_RIGHT){
					slot_level++;
					if(slot_level > 3) slot_level = 3;
					}
			if(keycode == KeyEvent.VK_LEFT){
				slot_level--;
				if(slot_level < 1) slot_level = 1;
				}
			if(keycode == KeyEvent.VK_ENTER) gamestate = SLOT_PLAY_POINT;
				break;
			case SLOT_PLAY_POINT:
				if(keycode == KeyEvent.VK_BACK_SPACE){
					setting_in = 0;
					gamestate = SLOT_PLAY_SELECT2;
				}
				if(keycode == KeyEvent.VK_ENTER){
					slot_state = 0;//choice,kekka,endの判別用。
					count = 0;//何回遊んだかを記録
					enter_state = 0;//エンターキーを制御
					slot_reel = 4;//0(全回転),1(1つ目が停止),2(2つ目が停止),3(3つ目が停止),4(全て停止)
					if(slot_level == 1){
						ran1 = rnd.nextInt(4);
						ran2 = rnd.nextInt(4);
						ran3 = rnd.nextInt(4);
					}
					if(slot_level == 2){
						ran1 = rnd.nextInt(7);
						ran2 = rnd.nextInt(7);
						ran3 = rnd.nextInt(7);
					}
					if(slot_level == 3){
						ran1 = rnd.nextInt(10);
						ran2 = rnd.nextInt(10);
						ran3 = rnd.nextInt(10);
					}
					a1 = 0;
					a2 = 0;
					a3 = 0;
					slot_pointn = 0;//ポイントを0に初期化
					slot_pointn_sum = 0;//合計ポイントも初期化
					slot_string = 1;//1で「エンターを押すと停止」、2で「1,2,3でリール停止」を表示。
					gamestate = SLOT_PLAY;
				}
				break;
			case SLOT_PLAY:
				if(keycode == KeyEvent.VK_1){
					if(enter_state == 1){
						reel_1 = 0; //リール1を止める
					    a1 = 1;
					}
				}
				if(keycode == KeyEvent.VK_2){
					if(enter_state == 1){
						reel_2 = 0; //リール2を止める
					    a2 = 1;
					}
				}
				if(keycode == KeyEvent.VK_3){
					if(enter_state == 1){
						reel_3 = 0; //リール3を止める
					    a3 = 1;
					}
				}
				if(keycode == KeyEvent.VK_ENTER){
					switch(enter_state){
					case 0://0の時はエンターが押せる(リールを回転させるのに使用)。
						if(slot_level == 1){
							reel_1 = 1;
							reel_2 = 1;
							reel_3 = 1;
						}
						if(slot_level == 2){
							reel_1 = 2;
							reel_2 = 2;
							reel_3 = 2;
						}
						if(slot_level == 3){
							reel_1 = 3;
							reel_2 = 3;
							reel_3 = 3;
						}

						count++;
						slot_string = 2;
						enter_state = 1;
						break;
					case 1://1の時はエンターが押せない(1,2,3のみ押せる)。
						break;
					case 2://2の時は押せる(結果表示時に使用)。
						try{
							PrintWriter pw = new PrintWriter(new BufferedWriter(
									new FileWriter("Uranai_savedata.txt")));

							String hl_3_1s = String.valueOf(hl_3_1);
							pw.println(hl_3_1s);
							String hl_3_2s = String.valueOf(hl_3_2);
							pw.println(hl_3_2s);
							String hl_3_3s = String.valueOf(hl_3_3);
							pw.println(hl_3_3s);

							String hl_5_1s = String.valueOf(hl_5_1);
							pw.println(hl_5_1s);
							String hl_5_2s = String.valueOf(hl_5_2);
							pw.println(hl_5_2s);
							String hl_5_3s = String.valueOf(hl_5_3);
							pw.println(hl_5_3s);
							String hl_5_4s = String.valueOf(hl_5_4);
							pw.println(hl_5_4s);

							String hl_10_1s = String.valueOf(hl_10_1);
							pw.println(hl_10_1s);
							String hl_10_2s = String.valueOf(hl_10_2);
							pw.println(hl_10_2s);
							String hl_10_3s = String.valueOf(hl_10_3);
							pw.println(hl_10_3s);
							String hl_10_4s = String.valueOf(hl_10_4);
							pw.println(hl_10_4s);
							String hl_10_5s = String.valueOf(hl_10_5);
							pw.println(hl_10_5s);

							String slot_pointn_maxs = String.valueOf(slot_pointn_max);
							pw.println(slot_pointn_maxs);

							String players = String.valueOf(player);
							pw.println(players);

							String mikuji_players = String.valueOf(mikuji_player);
							pw.println(mikuji_players);
							String seiza_players = String.valueOf(seiza_player);
							pw.println(seiza_players);
							String hl_players = String.valueOf(hl_player);
							pw.println(hl_players);
							String slot_players = String.valueOf(slot_player);
							pw.println(slot_players);

							String seiza_days = String.valueOf(seiza_day);
							pw.println(seiza_days);

							pw.close();
						}catch(IOException e){
							System.out.println("入出力エラー");
						}

						yl=0;
						yr=0;
						player++;
						slot_player++;
						gamestate = SLOT_EXIT;
						break;
					}
				}
				break;


			case SETTING:    //設定画面
				if(keycode == KeyEvent.VK_1) setting_state = 1;
				if(keycode == KeyEvent.VK_2) setting_state = 3;
				if(keycode == KeyEvent.VK_3) setting_state = 5;
				if(keycode == KeyEvent.VK_4) gamestate = SEIZA_MAIN;
				if(keycode == KeyEvent.VK_BACK_SPACE){
					if(setting_state != 0) setting_state = 0;
					else{
						setting_in = 0;
						gamestate = HL_MAIN;
					}
				}
				if(keycode == KeyEvent.VK_LEFT){
					if(setting_state == 5) seiza_day = 1;
				}
				if(keycode == KeyEvent.VK_RIGHT){
					if(setting_state == 5) seiza_day = 2;
				}
				if(keycode == KeyEvent.VK_ENTER){
					switch(setting_state){
					case 0:
						yl=0;
						yr=0;
						setting_in = 0;
						gamestate = HL_MAIN;
						break;
					case 1:
						setting_in = 0;
						setting_state = 2;
						break;
					case 3:
						setting_in = 0;
						waittimer = 30;

						try{
							BufferedReader br = new BufferedReader(new FileReader("Uranai_savedata.txt"));

							String hl_3_1s = br.readLine();
							hl_3_1 = Integer.parseInt(hl_3_1s);
							String hl_3_2s = br.readLine();
							hl_3_2 = Integer.parseInt(hl_3_2s);
							String hl_3_3s = br.readLine();
							hl_3_3 = Integer.parseInt(hl_3_3s);

							String hl_5_1s = br.readLine();
							hl_5_1 = Integer.parseInt(hl_5_1s);
							String hl_5_2s = br.readLine();
							hl_5_2 = Integer.parseInt(hl_5_2s);
							String hl_5_3s = br.readLine();
							hl_5_3 = Integer.parseInt(hl_5_3s);
							String hl_5_4s = br.readLine();
							hl_5_4 = Integer.parseInt(hl_5_4s);

							String hl_10_1s = br.readLine();
							hl_10_1 = Integer.parseInt(hl_10_1s);
							String hl_10_2s = br.readLine();
							hl_10_2 = Integer.parseInt(hl_10_2s);
							String hl_10_3s = br.readLine();
							hl_10_3 = Integer.parseInt(hl_10_3s);
							String hl_10_4s = br.readLine();
							hl_10_4 = Integer.parseInt(hl_10_4s);
							String hl_10_5s = br.readLine();
							hl_10_5 = Integer.parseInt(hl_10_5s);

							String slot_pointn_maxs = br.readLine();
							slot_pointn_max = Integer.parseInt(slot_pointn_maxs);

							String players = br.readLine();
							player = Integer.parseInt(players);

							String mikuji_players = br.readLine();
							mikuji_player = Integer.parseInt(mikuji_players);
							String seiza_players = br.readLine();
							seiza_player = Integer.parseInt(seiza_players);
							String hl_players = br.readLine();
							hl_player = Integer.parseInt(hl_players);
							String slot_players = br.readLine();
							slot_player = Integer.parseInt(slot_players);

							String seiza_days = br.readLine();
							seiza_day = Integer.parseInt(seiza_days);

							br.close();
						}catch(IOException e){
							System.out.println("入出力エラー");
						}

						try{
						PrintWriter pw = new PrintWriter(new BufferedWriter(
								new FileWriter("Uranai_savedata.txt")));

						String zero = String.valueOf(0);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						pw.println(zero);
						String players = String.valueOf(player);
						pw.println(players);
						String mikuji_players = String.valueOf(mikuji_player);
						pw.println(mikuji_players);
						String seiza_players = String.valueOf(seiza_player);
						pw.println(seiza_players);
						String hl_players = String.valueOf(hl_player);
						pw.println(hl_players);
						String slot_players = String.valueOf(slot_player);
						pw.println(slot_players);
						String seiza_days = String.valueOf(seiza_day);
						pw.println(seiza_days);

						pw.close();
						}catch(IOException e){
							System.out.println("入出力エラー");
						}

						hl_3_1 = 0;
						hl_3_2 = 0;
					    hl_3_3 = 0;
						hl_5_1 = 0;
						hl_5_2 = 0;
						hl_5_3 = 0;
						hl_5_4 = 0;
						hl_10_1 = 0;
						hl_10_2 = 0;
						hl_10_3 = 0;
						hl_10_4 = 0;
						hl_10_5 = 0;
						slot_pointn_max = 0;

						setting_state = 4;
						break;
					case 5:
						try{
							BufferedReader br = new BufferedReader(new FileReader("Uranai_savedata.txt"));

							String hl_3_1s = br.readLine();
							hl_3_1 = Integer.parseInt(hl_3_1s);
							String hl_3_2s = br.readLine();
							hl_3_2 = Integer.parseInt(hl_3_2s);
							String hl_3_3s = br.readLine();
							hl_3_3 = Integer.parseInt(hl_3_3s);

							String hl_5_1s = br.readLine();
							hl_5_1 = Integer.parseInt(hl_5_1s);
							String hl_5_2s = br.readLine();
							hl_5_2 = Integer.parseInt(hl_5_2s);
							String hl_5_3s = br.readLine();
							hl_5_3 = Integer.parseInt(hl_5_3s);
							String hl_5_4s = br.readLine();
							hl_5_4 = Integer.parseInt(hl_5_4s);

							String hl_10_1s = br.readLine();
							hl_10_1 = Integer.parseInt(hl_10_1s);
							String hl_10_2s = br.readLine();
							hl_10_2 = Integer.parseInt(hl_10_2s);
							String hl_10_3s = br.readLine();
							hl_10_3 = Integer.parseInt(hl_10_3s);
							String hl_10_4s = br.readLine();
							hl_10_4 = Integer.parseInt(hl_10_4s);
							String hl_10_5s = br.readLine();
							hl_10_5 = Integer.parseInt(hl_10_5s);

							String slot_pointn_maxs = br.readLine();
							slot_pointn_max = Integer.parseInt(slot_pointn_maxs);

							br.close();
						}catch(IOException e){
							System.out.println("入出力エラー");
						}

						try{
							PrintWriter pw = new PrintWriter(new BufferedWriter(
									new FileWriter("Uranai_savedata.txt")));

							String hl_3_1s = String.valueOf(hl_3_1);
							pw.println(hl_3_1s);
							String hl_3_2s = String.valueOf(hl_3_2);
							pw.println(hl_3_2s);
							String hl_3_3s = String.valueOf(hl_3_3);
							pw.println(hl_3_3s);

							String hl_5_1s = String.valueOf(hl_5_1);
							pw.println(hl_5_1s);
							String hl_5_2s = String.valueOf(hl_5_2);
							pw.println(hl_5_2s);
							String hl_5_3s = String.valueOf(hl_5_3);
							pw.println(hl_5_3s);
							String hl_5_4s = String.valueOf(hl_5_4);
							pw.println(hl_5_4s);

							String hl_10_1s = String.valueOf(hl_10_1);
							pw.println(hl_10_1s);
							String hl_10_2s = String.valueOf(hl_10_2);
							pw.println(hl_10_2s);
							String hl_10_3s = String.valueOf(hl_10_3);
							pw.println(hl_10_3s);
							String hl_10_4s = String.valueOf(hl_10_4);
							pw.println(hl_10_4s);
							String hl_10_5s = String.valueOf(hl_10_5);
							pw.println(hl_10_5s);

							String slot_pointn_maxs = String.valueOf(slot_pointn_max);
							pw.println(slot_pointn_maxs);

							String players = String.valueOf(player);
							pw.println(players);

							String mikuji_players = String.valueOf(mikuji_player);
							pw.println(mikuji_players);
							String seiza_players = String.valueOf(seiza_player);
							pw.println(seiza_players);
							String hl_players = String.valueOf(hl_player);
							pw.println(hl_players);
							String slot_players = String.valueOf(slot_player);
							pw.println(slot_players);

							String seiza_days = String.valueOf(seiza_day);
							pw.println(seiza_days);

							pw.close();
						}catch(IOException e){
							System.out.println("入出力エラー");
						}
						break;
					}
				}
				break;
			}
		}

		public void keyTyped(KeyEvent ev){
			char keycode = ev.getKeyChar();
		}
	}


	class TT extends TimerTask{
		public void run(){
			Graphics g = bs.getDrawGraphics();
			if(bs.contentsLost()==false){
				Insets insets = frame.getInsets();
				g.translate(insets.left,insets.top);

				switch(gamestate){
				case MIKUJI_MAIN:
					clear(g);

					waittimer--;
					if(waittimer < 0) setting_in = 0;

					g.drawImage(mikuji_main,0,0,w,h,frame);
					g.drawImage(yazirusi_left,(20+(int)yl),(h/2),50,50,frame);
					g.drawImage(yazirusi_right,((w-80)+(int)yr),(h/2),50,50,frame);
					yl-=0.4; if(yl < -25) yl = 0;
					yr+=0.4; if(yr > 25) yr = 0;
					g.setColor(Color.WHITE);
					g.setFont(new Font("Selif",Font.BOLD,20));
					g.drawString("スロット",20,(h/2)+80);
					g.setColor(Color.WHITE);
					g.drawString("ハイ＆ロー",(w-120),(h/2)+80);
					break;
				case SEIZA_MAIN:
					clear(g);

					waittimer--;
					if(waittimer < 0) setting_in = 0;

					g.drawImage(seiza_main,0,0,w,h,frame);
					g.drawImage(yazirusi_left,(20+(int)yl),(h/2),50,50,frame);
					g.drawImage(yazirusi_right,((w-80)+(int)yr),(h/2),50,50,frame);
					yl-=0.4; if(yl < -25) yl = 0;
					yr+=0.4; if(yr > 25) yr = 0;
					g.setColor(Color.BLACK);
					g.setFont(new Font("Selif",Font.BOLD,20));
					g.drawString("ハイ＆ロー",20,(h/2)+80);
					g.drawString("スロット",(w-120),(h/2)+80);
					break;
				case HL_MAIN:
					clear(g);

					waittimer--;
					if(waittimer < 0) setting_in = 0;

					g.drawImage(hl_main,0,0,w,h,frame);
					g.drawImage(yazirusi_left,(20+(int)yl),(h/2),50,50,frame);
					g.drawImage(yazirusi_right,((w-80)+(int)yr),(h/2),50,50,frame);
					yl-=0.4; if(yl < -25) yl = 0;
					yr+=0.4; if(yr > 25) yr = 0;
					g.setColor(Color.BLACK);
					g.setFont(new Font("Selif",Font.BOLD,20));
					g.drawString("おみくじ",20,(h/2)+80);
					g.drawString("スロット",(w-120),(h/2)+80);
					break;
				case SLOT_MAIN:
					clear(g);

					waittimer--;
					if(waittimer < 0) setting_in = 0;

					g.drawImage(slot_main,0,0,w,h,frame);
					g.drawImage(yazirusi_left,(20+(int)yl),(h/2),50,50,frame);
					g.drawImage(yazirusi_right,((w-80)+(int)yr),(h/2),50,50,frame);
					yl-=0.4; if(yl < -25) yl = 0;
					yr+=0.4; if(yr > 25) yr = 0;
					g.setColor(Color.WHITE);
					g.setFont(new Font("Selif",Font.BOLD,20));
					g.drawString("ハイ＆ロー",20,(h/2)+80);
					g.drawString("おみくじ",(w-120),(h/2)+80);
					break;


				case MIKUJI_START:
					clear(g);
					gamestate=MIKUJI_PLAY;
					break;
				case SEIZA_START:
					clear(g);
					gamestate=SEIZA_PLAY;
					break;
				case HL_START:
					clear(g);
					gamestate=HL_SELECT;
					break;
				case SLOT_START:
					clear(g);
					gamestate=SLOT_PLAY_SELECT1;
					break;


				case MIKUJI_PLAY:
					clear(g);
					MIKUJI_GAME(g);
					break;
				case MIKUJI_PLAY_KEKKA:
					clear(g);
					MIKUJI_GAME_KEKKA(g);
					break;
				case SEIZA_PLAY:
					clear(g);
					SEIZA_GAME(g);
				    break;
				case SEIZA_PLAY_KEKKA:
					clear(g);
					SEIZA_GAME_KEKKA(g);
					break;
				case HL_SELECT:
					clear(g);
					HL_GAME_SELECT(g);
					break;
				case HL_PLAY:
					clear(g);
					HL_GAME(g);
					break;
				case SLOT_PLAY_SELECT1:
					clear(g);
					SLOT_GAME_SELECT1(g);
					break;
				case SLOT_PLAY_SELECT2:
					clear(g);
					SLOT_GAME_SELECT2(g);
					break;
				case SLOT_PLAY_POINT:
					clear(g);
					SLOT_GAME_POINT(g);
					break;
				case SLOT_PLAY:
					clear(g);
					SLOT_GAME(g);
					break;


				case MIKUJI_EXIT:
					clear(g);
					nums = "";
					gamestate=MIKUJI_MAIN;
					break;
				case SEIZA_EXIT:
					clear(g);
					gamestate=SEIZA_MAIN;
					break;
				case HL_EXIT:
					clear(g);
					gamestate=HL_MAIN;
					break;
				case SLOT_EXIT:
					clear(g);
					gamestate=SLOT_MAIN;
					break;


				case SETTING:
					clear(g);
					SETTING(g);
					break;
				}
				bs.show();
				g.dispose();
			}
		}


		void drawStringCenter(String str,int y,Graphics g){
			//文字を真ん中に表示するメソッド。yは文字をどの高さに表示するかの値。
			int fw = frame.getWidth() / 2;
			FontMetrics fm = g.getFontMetrics();
			int strw = fm.stringWidth(str) / 2;
			g.drawString(str,fw-strw,y);
		}

		public void clear(Graphics g){
			//画面をクリアするメソッド。
			g.clearRect(0,0,w,h);
		}


		//○○_GAME・・・ゲームの本編を記述する。
		public void MIKUJI_GAME(Graphics g){
			g.drawImage(mikuji_num,0,0,w,h,frame);
			g.setFont(new Font("Selif",Font.BOLD,38));
			drawStringCenter("矢印キーで好きな色を選び、エンターキーで決定してください！",180,g);
			if(select == 1){
				g.drawImage(blue_sel,120,480,260,160,frame);
			}else{
				g.drawImage(blue,120,480,260,160,frame);
			}
			if(select == 2){
				g.drawImage(red_sel,420,480,260,160,frame);
			}else{
				g.drawImage(red,420,480,260,160,frame);
			}
			if(select == 3){
				g.drawImage(yellow_sel,720,480,260,160,frame);
			}else{
				g.drawImage(yellow,720,480,260,160,frame);
			}
			if(select == 4){
				g.drawImage(green_sel,1020,480,260,160,frame);
			}else{
				g.drawImage(green,1020,480,260,160,frame);
			}
		}
		public void dI_sID(Graphics g){
			//背景を描写＆ダイアログを表示するメソッド(drawImage_showInputDialog)。
			nums = JOptionPane.showInputDialog(
					frame,"1～50の中から好きな数字を1つ入力してください！\n"
							+ "(入力が完了したらエンターを押してください。)");
			gamestate = MIKUJI_MAIN;
			scheduler();
			/*try{
				num = Integer.parseInt(nums);
				if(num <1 || num >50){
					nums = "www";
					num = Integer.parseInt(nums);
				}
				//gamestate = MIKUJI_PLAY_2;
				gamestate = MIKUJI_MAIN;
				scheduler();
			}catch(Exception e){
				gamestate = MIKUJI_MAIN;
				scheduler();
				JOptionPane.showMessageDialog(
						frame,"1～50の中の数字を入力して下さい。");
				dI_sID(g);
			}*/
		}
		public void MIKUJI_GAME_KEKKA(Graphics g){
			g.drawImage(mikuji_num,0,0,w,h,frame);
			g.setFont(new Font("Selif",Font.BOLD,35));
			if(select == 1){
				g.drawImage(blue_sel,120,480,260,160,frame);
			}else{
				g.drawImage(blue,120,480,260,160,frame);
			}
			if(select == 2){
				g.drawImage(red_sel,420,480,260,160,frame);
			}else{
				g.drawImage(red,420,480,260,160,frame);
			}
			if(select == 3){
				g.drawImage(yellow_sel,720,480,260,160,frame);
			}else{
				g.drawImage(yellow,720,480,260,160,frame);
			}
			if(select == 4){
				g.drawImage(green_sel,1020,480,260,160,frame);
			}else{
				g.drawImage(green,1020,480,260,160,frame);
			}

			waittimer--;
			if(waittimer < 0){
				g.drawImage(hoshi[ran],180,200,130,250,frame);
				g.drawImage(hoshi[ran1],480,200,130,250,frame);
				g.drawImage(hoshi[ran2],780,200,130,250,frame);
				g.drawImage(hoshi[ran3],1080,200,130,250,frame);

				if(select == 1){
					String rans = String.valueOf(ran);
					drawStringCenter("青を選んだあなたは  "+rans+"ポイントでした。",120,g);
				}
				if(select == 2){
					String ran1s = String.valueOf(ran1);
					drawStringCenter("赤を選んだあなたは  "+ran1s+"ポイントでした。",120,g);
				}
				if(select == 3){
					String ran2s = String.valueOf(ran2);
					drawStringCenter("黄色を選んだあなたは  "+ran2s+"ポイントでした。",120,g);
				}
				if(select == 4){
					String ran3s = String.valueOf(ran3);
					drawStringCenter("緑を選んだあなたは  "+ran3s+"ポイントでした。",120,g);
				}
				drawStringCenter("エンターキーでゲーム終了",700,g);

				enter_state = 2;
			}

			/*g.drawImage(mikuji_kami,0,0,w-8,h-30,frame);
			g.setFont(new Font("Selif",Font.BOLD,50));
			switch(ran){
			case 1:
				//g.drawImage(mikuji_daikiti,0,0,w,h,frame);
				g.drawString("大",w-130,240);
				g.drawString("吉",w-130,360);
				break;
			case 2:
				//g.drawImage(mikuji_kiti,0,0,w,h,frame);
				g.drawString("吉",w-130,360);
				break;
			case 3:
			    //g.drawImage(mikuji_chuukiti,0,0,w,h,frame);
				g.drawString("中",w-130,240);
				g.drawString("吉",w-130,360);
				break;
			case 4:
				//g.drawImage(mikuji_shoukiti,0,0,w,h,frame);
				g.drawString("小",w-130,240);
				g.drawString("吉",w-130,360);
				break;
			case 5:
				//g.drawImage(mikuji_suekiti,0,0,w,h,frame);
				g.drawString("末",w-130,240);
			    g.drawString("吉",w-130,360);
				break;
			case 6:
				//g.drawImage(mikuji_kyou,0,0,w,h,frame);
				g.drawString("凶",w-130,360);
				break;
			}*/
		}


		public void SEIZA_GAME(Graphics g){
			g.drawImage(seiza_haikei,0,0,w,h,frame);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Selif",Font.PLAIN,30));
			drawStringCenter("あなたの星座を選択してください。\n"
					+ "(矢印キーで選択、エンターキーで決定)",80,g);

			if(select == 1) g.drawImage(seiza_hituji_sel,140,250,90,90,frame);
			else g.drawImage(seiza_hituji,120,250,90,90,frame);
			if(select == 2) g.drawImage(seiza_oushi_sel,340,250,90,90,frame);
			else g.drawImage(seiza_oushi,320,250,90,90,frame);
			if(select == 3) g.drawImage(seiza_hutago_sel,540,250,90,90,frame);
			else g.drawImage(seiza_hutago,520,250,90,90,frame);
			if(select == 4) g.drawImage(seiza_kani_sel,740,250,90,90,frame);
			else g.drawImage(seiza_kani,720,250,90,90,frame);
			if(select == 5) g.drawImage(seiza_sisi_sel,940,250,90,90,frame);
			else g.drawImage(seiza_sisi,920,250,90,90,frame);
			if(select == 6) g.drawImage(seiza_otome_sel,1140,250,90,90,frame);
			else g.drawImage(seiza_otome,1120,250,90,90,frame);
			if(select == 7) g.drawImage(seiza_tenbin_sel,140,520,90,90,frame);
			else g.drawImage(seiza_tenbin,120,520,90,90,frame);
			if(select == 8) g.drawImage(seiza_sasori_sel,340,520,90,90,frame);
			else g.drawImage(seiza_sasori,320,520,90,90,frame);
			if(select == 9) g.drawImage(seiza_ite_sel,540,520,90,90,frame);
			else g.drawImage(seiza_ite,520,520,90,90,frame);
			if(select == 10) g.drawImage(seiza_yagi_sel,740,520,90,90,frame);
			else g.drawImage(seiza_yagi,720,520,90,90,frame);
			if(select == 11) g.drawImage(seiza_mizugame_sel,940,520,90,90,frame);
			else g.drawImage(seiza_mizugame,920,520,90,90,frame);
			if(select == 12) g.drawImage(seiza_uo_sel,1140,520,90,90,frame);
			else g.drawImage(seiza_uo,1120,520,90,90,frame);

			g.setFont(new Font("Selif",Font.PLAIN,20));
			g.drawString("3/21～4/19",120,375);
			g.drawString("4/20～5/20",320,375);
			g.drawString("5/21～6/21",520,375);
			g.drawString("6/22～7/22",720,375);
			g.drawString("7/23～8/22",920,375);
			g.drawString("8/23～9/22",1120,375);
			g.drawString("おひつじ座",120,230);
			g.drawString("おうし座",320,230);
			g.drawString("ふたご座",520,230);
			g.drawString("かに座",720,230);
			g.drawString("しし座",920,230);
			g.drawString("おとめ座",1120,230);
			g.setColor(new Color(0,0,0));
			g.drawString("9/23～10/23",120,645);
			g.drawString("10/24～11/21",320,645);
			g.drawString("11/22～12/21",520,645);
			g.drawString("12/22～1/19",720,645);
			g.drawString("1/20～2/18",920,645);
			g.drawString("2/19～3/20",1120,645);
			g.drawString("てんびん座",120,500);
			g.drawString("さそり座",320,500);
			g.drawString("いて座",520,500);
			g.drawString("やぎ座",720,500);
			g.drawString("みずがめ座",920,500);
			g.drawString("うお座",1120,500);

		}
		public void SEIZA_GAME_KEKKA(Graphics g){
			//String selects = String.valueOf(select);
			//g.drawImage(seiza_haikei,0,0,1366,768,frame);
			//g.drawString(selects,300,300);

			switch(select){
			case 1:
				if(seiza_day==1) g.drawImage(seiza_kekka[0],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[12],0,0,w,h,frame);
				break;
			case 2:
				if(seiza_day==1) g.drawImage(seiza_kekka[1],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[13],0,0,w,h,frame);
				break;
			case 3:
				if(seiza_day==1) g.drawImage(seiza_kekka[2],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[14],0,0,w,h,frame);
				break;
			case 4:
				if(seiza_day==1) g.drawImage(seiza_kekka[3],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[15],0,0,w,h,frame);
				break;
			case 5:
				if(seiza_day==1) g.drawImage(seiza_kekka[4],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[16],0,0,w,h,frame);
				break;
			case 6:
				if(seiza_day==1) g.drawImage(seiza_kekka[5],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[17],0,0,w,h,frame);
				break;
			case 7:
				if(seiza_day==1) g.drawImage(seiza_kekka[6],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[18],0,0,w,h,frame);
				break;
			case 8:
				if(seiza_day==1) g.drawImage(seiza_kekka[7],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[19],0,0,w,h,frame);
				break;
			case 9:
				if(seiza_day==1) g.drawImage(seiza_kekka[8],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[20],0,0,w,h,frame);
				break;
			case 10:
				if(seiza_day==1) g.drawImage(seiza_kekka[9],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[21],0,0,w,h,frame);
				break;
			case 11:
				if(seiza_day==1) g.drawImage(seiza_kekka[10],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[22],0,0,w,h,frame);
				break;
			case 12:
				if(seiza_day==1) g.drawImage(seiza_kekka[11],0,0,w,h,frame);
				else g.drawImage(seiza_kekka[23],0,0,w,h,frame);
				break;
			}
		}


		public void HL_GAME_SELECT(Graphics g){
			g.drawImage(trump_haikei,0,0,w,h,frame);
			g.setFont(new Font("Selif",Font.PLAIN,38));
			g.setColor(Color.BLACK);
			drawStringCenter("何回遊ぶかを選んでください。",160,g);
			drawStringCenter("(矢印キーで選択、エンターキーで決定)",230,g);
			g.drawImage(hl_select_back,100,360,1200,300,frame);
			if(select == 0){
				g.drawImage(hl_select3,200,400,300,120,frame);
			}else{
				g.setFont(new Font("Selif",Font.PLAIN,45));
				g.setColor(Color.BLACK);
				g.drawString("3回遊ぶ",270,470);
			}
			if(select == 1){
				g.drawImage(hl_select5,530,400,300,120,frame);
			}else{
				g.setFont(new Font("Selif",Font.PLAIN,45));
				g.setColor(Color.BLACK);
				g.drawString("5回遊ぶ",595,470);
			}
			if(select == 2){
				g.drawImage(hl_select10,860,400,300,120,frame);
			}else{
				g.setFont(new Font("Selif",Font.PLAIN,45));
				g.setColor(Color.BLACK);
				g.drawString("10回遊ぶ",920,470);
			}
			g.setFont(new Font("Selif",Font.PLAIN,35));
			g.setColor(Color.BLACK);
			g.drawString("所要時間:約8秒",230,630);
			g.drawString("所要時間:約15秒",555,630);
			g.drawString("所要時間:約30秒",880,630);
		}
		public void HL_GAME(Graphics g){
			switch(hl_state){
			case 0:
				HL_GAME_CHOICE(g);
				break;
			case 1:
				HL_GAME_KEKKA(g);
				break;
			case 2:
				HL_GAME_END(g);
				break;
			}
		}
		public void HL_GAME_CHOICE(Graphics g){
			if(hl_play == 3){
				if(count > 2){
						if(hl_win == 1){
							hl_3_1++;
						}
						else if(hl_win == 2){
							hl_3_2++;
						}
						else if(hl_win == 3){
							hl_3_3++;
						}

					enter_state = 2;
					hl_state = 2;
				}
			}
			if(hl_play == 5){
				if(count > 4){
						if(hl_win <= 1){
							hl_5_1++;
						}
						else if(hl_win >= 2 && hl_win <= 3){
							hl_5_2++;
						}
						else if(hl_win == 4){
							hl_5_3++;
						}
						else if(hl_win == 5){
							hl_5_4++;
						}

					enter_state = 2;
					hl_state = 2;
				}
			}
			if(hl_play == 10){
				if(count > 9){
						if(hl_win <= 2){
							hl_10_1++;
						}
						else if(hl_win >= 3 && hl_win <= 5){
							hl_10_2++;
						}
						else if(hl_win >= 6 && hl_win <= 7){
							hl_10_3++;
						}
						else if(hl_win >= 8 && hl_win <= 9){
							hl_10_4++;
						}
						else if(hl_win == 10){
							hl_10_5++;
						}

					enter_state = 2;
					hl_state = 2;
				}
			}

			g.drawImage(hl_haikei2,0,0,w,h,frame);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Selif",Font.PLAIN,30));
			g.drawString("NEXT\n  ?",300,80);
			String counts= String.valueOf(count+1);
			g.drawString(counts,1000,200);
			if(hl_play == 3) g.drawString("/3",1040,200);
			if(hl_play == 5) g.drawString("/5",1040,200);
			if(hl_play == 10) g.drawString("/10",1040,200);
			g.drawImage(trump_ura,300,100,140,190,frame);
			g.drawImage(trump[ran],610,80,140,190,frame);//カードは画面中央に配置する。
		//	ran_before = ran;
			g.setFont(new Font("Selif",Font.BOLD,40));
			drawStringCenter("次のカードはこのカードの目よりも大きい(High)?小さい(Low)?",
					400,g);
			g.setFont(new Font("Selif",Font.BOLD,35));
			if(select == 1){
				g.drawImage(high,310,500,200,100,frame);
			}else{
				g.setColor(Color.RED);
				g.drawString("High",340,540);
			}
			if(select == 2){
				g.drawImage(low,880,500,200,100,frame);
			}else{
				g.setColor(Color.BLUE);
				g.drawString("Low",910,540);
			}
		}
		public void HL_GAME_KEKKA(Graphics g){
			clear(g);
			g.drawImage(hl_haikei2,0,0,w,h,frame);
			g.drawImage(trump[ran],300,100,140,190,frame);
			g.drawImage(trump[ran_before],610,80,140,190,frame);//カードは画面中央に配置する

			g.setColor(Color.BLACK);
			g.setFont(new Font("Selif",Font.BOLD,45));
			if(select == 1){ //Highを選択した時の処理
				if(ran_before >= ran-(ran%4) && ran_before <= (ran-(ran%4))+3){
					//ran_beforeとranが同じ数字のトランプだったら
					count_state = 0;
					win_state = 0;
					drawStringCenter("-EVEN-",540,g);
				}
				else if(ran > ran_before){
					count_state = 1;
					win_state = 1;
					drawStringCenter("YOU WIN!!",540,g);
				}
				else if(ran < ran_before){
					count_state = 1;
					win_state = 0;
					drawStringCenter("YOU LOSE...",540,g);
				}
			}
			if(select == 2){ //Lowを選択した時の処理
				if(ran_before >= ran-(ran%4) && ran_before <= (ran-(ran%4))+3){
					count_state = 0;
					win_state = 0;
					drawStringCenter("-EVEN-",540,g);
				}
				else if(ran < ran_before){
					count_state = 1;
					win_state = 1;
					drawStringCenter("YOU WIN!!",540,g);
				}
				else if(ran > ran_before){
					count_state = 1;
					win_state = 0;
					drawStringCenter("YOU LOSE...",540,g);
				}
			}
			waittimer--;
			if(waittimer < 0){
				if(count_state == 1) count++;
				if(win_state == 1) hl_win++;

				enter_state = 0;
				hl_state = 0;
			}
		}
		public void HL_GAME_END(Graphics g){
			g.drawImage(trump_haikei,0,0,w,h,frame);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Selif",Font.PLAIN,40));
			String win = String.valueOf(hl_win);
			if(hl_play == 3) drawStringCenter("勝った回数: "+win+"  /3",120,g);
			if(hl_play == 5) drawStringCenter("勝った回数: "+win+"  /5",120,g);
			if(hl_play == 10) drawStringCenter("勝った回数: "+win+"  /10",120,g);

			if(hl_play == 3){
				if(hl_win <= 1){
					drawStringCenter("Sorry...Let's try again!",180,g);
					String hl_3_1s = String.valueOf(hl_3_1);
					drawStringCenter("本日"+hl_3_1s+"人目",390,g);
				}
				else if(hl_win == 2){
					drawStringCenter("Good!!(_・ω・ )☆",180,g);
					String hl_3_2s = String.valueOf(hl_3_2);
					drawStringCenter("本日"+hl_3_2s+"人目",390,g);
				}
				else if(hl_win == 3){
					g.setColor(Color.RED);
					drawStringCenter("VERY GOOD!! PERFECT!!",180,g);
					String hl_3_3s = String.valueOf(hl_3_3);
					drawStringCenter("本日"+hl_3_3s+"人目",390,g);
				}
			}

			if(hl_play == 5){
				if(hl_win <= 1){
					drawStringCenter("Sorry...Let's try again!",180,g);
					String hl_5_1s = String.valueOf(hl_5_1);
					drawStringCenter("本日"+hl_5_1s+"人目",390,g);
				}
				else if(hl_win >= 2 && hl_win <= 3){
					drawStringCenter("Good!!d(･∀･○)",180,g);
					String hl_5_2s = String.valueOf(hl_5_2);
					drawStringCenter("本日"+hl_5_2s+"人目",390,g);
				}
				else if(hl_win == 4){
					g.setColor(Color.RED);
					drawStringCenter("VERY GOOD!!",180,g);
					String hl_5_3s = String.valueOf(hl_5_3);
					drawStringCenter("本日"+hl_5_3s+"人目",390,g);
				}
				else if(hl_win == 5){
					g.setColor(new Color(218,165,32));
					drawStringCenter("---PERFECT---",180,g);
					drawStringCenter("AMAZING.",270,g);
					String hl_5_4s = String.valueOf(hl_5_4);
					drawStringCenter("本日"+hl_5_4s+"人目",390,g);
				}
			}
			if(hl_play == 10){
				if(hl_win <= 2){
					drawStringCenter("Sorry...Let's try again!",180,g);
					String hl_10_1s = String.valueOf(hl_10_1);
					drawStringCenter("本日"+hl_10_1s+"人目",390,g);
				}
				else if(hl_win >= 3 && hl_win <= 5){
					drawStringCenter("(*・∀・*)bGOOD!",180,g);
					String hl_10_2s = String.valueOf(hl_10_2);
					drawStringCenter("本日"+hl_10_2s+"人目",390,g);
				}
				else if(hl_win >= 6 && hl_win <= 7){
					g.setColor(Color.RED);
					drawStringCenter("VERY GOOD!!",180,g);
					String hl_10_3s = String.valueOf(hl_10_3);
					drawStringCenter("本日"+hl_10_3s+"人目",390,g);
				}
				else if(hl_win >= 8 && hl_win <= 9){
					g.setColor(new Color(218,165,32));
					drawStringCenter("---unbelievable---",180,g);
					drawStringCenter("AMAZING .",270,g);
					String hl_10_4s = String.valueOf(hl_10_4);
					drawStringCenter("本日"+hl_10_4s+"人目",390,g);
				}
				else if(hl_win == 10){
					g.setColor(new Color(199,21,133));
					drawStringCenter("Oh My God...! Are you LEGEND?",180,g);
					String hl_10_5s = String.valueOf(hl_10_5);
					drawStringCenter("本日"+hl_10_5s+"人目",390,g);
				}
			}

			try{
				PrintWriter pw = new PrintWriter(new BufferedWriter(
						new FileWriter("Uranai_savedata.txt")));

				String hl_3_1s = String.valueOf(hl_3_1);
				pw.println(hl_3_1s);
				String hl_3_2s = String.valueOf(hl_3_2);
				pw.println(hl_3_2s);
				String hl_3_3s = String.valueOf(hl_3_3);
				pw.println(hl_3_3s);

				String hl_5_1s = String.valueOf(hl_5_1);
				pw.println(hl_5_1s);
				String hl_5_2s = String.valueOf(hl_5_2);
				pw.println(hl_5_2s);
				String hl_5_3s = String.valueOf(hl_5_3);
				pw.println(hl_5_3s);
				String hl_5_4s = String.valueOf(hl_5_4);
				pw.println(hl_5_4s);

				String hl_10_1s = String.valueOf(hl_10_1);
				pw.println(hl_10_1s);
				String hl_10_2s = String.valueOf(hl_10_2);
				pw.println(hl_10_2s);
				String hl_10_3s = String.valueOf(hl_10_3);
				pw.println(hl_10_3s);
				String hl_10_4s = String.valueOf(hl_10_4);
				pw.println(hl_10_4s);
				String hl_10_5s = String.valueOf(hl_10_5);
				pw.println(hl_10_5s);

				String slot_pointn_maxs = String.valueOf(slot_pointn_max);
				pw.println(slot_pointn_maxs);

				String players = String.valueOf(player+1);
				pw.println(players);

				String mikuji_players = String.valueOf(mikuji_player);
				pw.println(mikuji_players);
				String seiza_players = String.valueOf(seiza_player);
				pw.println(seiza_players);
				String hl_players = String.valueOf(hl_player);
				pw.println(hl_players);
				String slot_players = String.valueOf(slot_player);
				pw.println(slot_players);

				String seiza_days = String.valueOf(seiza_day);
				pw.println(seiza_days);

				pw.close();
			}catch(IOException e){
				System.out.println("入出力エラー");
			}

			drawStringCenter("エンターキーを押してください。",500,g);
		}


		public void SLOT_GAME_SELECT1(Graphics g){
				g.setColor(new Color(240,230,140));
				g.fillRect(0,0,w,h);
				g.setColor(Color.BLACK);
				g.setFont(new Font("Selif",Font.PLAIN,40));
				drawStringCenter("何回遊ぶかを選択してください.",140,g);
				drawStringCenter("(←,→で選択、エンターキーで決定)",200,g);
				if(select == 0){
					g.drawImage(slot_select_3,200,400,300,140,frame);
				}else{
					g.setFont(new Font("Selif",Font.PLAIN,50));
					g.drawString("3回",280,470);
				}
				if(select == 1){
					g.drawImage(slot_select_5,530,400,300,140,frame);
				}else{
					g.setFont(new Font("Selif",Font.PLAIN,50));
					g.drawString("5回",610,470);
				}
				if(select == 2){
					g.drawImage(slot_select_10,860,400,300,140,frame);
				}else{
					g.drawString("10回",940,470);
				}

			g.setFont(new Font("Selif",Font.PLAIN,35));
			g.drawString("所要時間:約15秒",210,600);
			g.drawString("所要時間:約25秒",545,600);
			g.drawString("所要時間:約50秒",880,600);
		}
		public void SLOT_GAME_SELECT2(Graphics g){
			g.setColor(new Color(240,230,140));
			g.fillRect(0,0,w,h);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Selif",Font.PLAIN,40));
			drawStringCenter("ゲームの難易度を選択してください.",140,g);
			drawStringCenter("(←,→で選択、エンターキーで決定)",200,g);

			g.setFont(new Font("Selif",Font.PLAIN,32));
			drawStringCenter("EASY・・・スロットの目が1～3(+ダイヤ)しかありません（簡単）。",320,g);
			drawStringCenter("NORMAL・・・スロットの目が1～6(+ダイヤ)まであります（ふつう）。",380,g);
			drawStringCenter("HARD・・・スロットの目が1～9(+ダイヤ)あります(難しい)。",440,g);

			g.setFont(new Font("Selif",Font.PLAIN,40));
			if(slot_level == 1){
				g.drawImage(slot_easy,100,550,300,140,frame);
			}else{
				g.drawString("EASY",150,600);
			}
			if(slot_level == 2){
				g.drawImage(slot_normal,550,550,300,140,frame);
			}else{
				g.drawString("NORMAL",600,600);
			}
			if(slot_level == 3){
				g.drawImage(slot_hard,1000,550,300,140,frame);
			}else{
				g.drawString("HARD",1050,600);
			}
		}
		public void SLOT_GAME_POINT(Graphics g){
			g.drawImage(slot_point,0,0,w,h,frame);
		}
		public void SLOT_GAME(Graphics g){
				switch(slot_state){
				case 0:
					SLOT_GAME_CHOICE(g);
					break;
				case 1:
					SLOT_GAME_KEKKA(g);
					break;
				case 2:
					SLOT_GAME_END(g);
					break;
			}
		}
		public void SLOT_GAME_CHOICE(Graphics g){
			clear(g);

			g.setColor(new Color(240,230,140));
			g.fillRect(0,0,w,h);

			g.drawImage(slot[ran1],300,200,160,210,frame);
			g.drawImage(slot[ran2],610,200,160,210,frame);
			g.drawImage(slot[ran3],900,200,160,210,frame);
			g.setFont(new Font("Selif",Font.PLAIN,40));
			g.setColor(Color.BLACK);
			g.drawString("1で停止",310,470);
			g.drawString("2で停止",620,470);
			g.drawString("3で停止",910,470);

			String counts = String.valueOf(count);
			g.drawString(counts+"ゲーム目",1020,185);
			if(slot_play == 3) g.drawString("/3",1230,185);
			if(slot_play == 5) g.drawString("/5",1230,185);
			if(slot_play == 10) g.drawString("/10",1230,185);

			switch(reel_1){ //リール1について
			case 1: //easy
				ran1++;
				if(ran1 > 3) ran1 = 0;
				break;
			case 2: //normal
				ran1++;
				if(ran1 > 6) ran1 = 0;
				break;
			case 3: //hard
				ran1++;
				if(ran1 > 9) ran1 = 0;
				break;
			}
			switch(reel_2){ //リール2について
			case 1: //easy
				ran2++;
				if(ran2 > 3) ran2 = 0;
				break;
			case 2: //normal
				ran2++;
				if(ran2 > 6) ran2 = 0;
				break;
			case 3: //hard
				ran2++;
				if(ran2 > 9) ran2 = 0;
				break;
			}
			switch(reel_3){ //リール3について
			case 1: //easy
				ran3++;
				if(ran3 > 3) ran3 = 0;
				break;
			case 2: //normal
				ran3++;
				if(ran3 > 6) ran3 = 0;
				break;
			case 3: //hard
				ran3++;
				if(ran3 > 9) ran3 = 0;
				break;
			}

			switch(slot_string){
			case 1:
				g.setColor(new Color(240,230,140));
				g.fillRect(0,520,w,240);
				g.setFont(new Font("Selif",Font.BOLD,40));
				g.setColor(Color.BLACK);
				drawStringCenter("エンターキーでスタート!",140,g);
				break;
			case 2:
				g.setColor(new Color(240,230,140));
				g.fillRect(0,520,w,240);
				g.setFont(new Font("Selif",Font.BOLD,40));
				g.setColor(Color.BLACK);
				drawStringCenter("1,2,3を押すとリールがそれぞれ止まります",140,g);
				break;
			}

			if((a1+a2+a3) == 3){
				waittimer = 30;
				slot_state = 1;
			}

		}
		public void SLOT_GAME_KEKKA(Graphics g){
			clear(g);
			g.setColor(new Color(240,230,140));
			g.fillRect(0,0,w,h);

			g.drawImage(slot[ran1],300,200,160,210,frame);
			g.drawImage(slot[ran2],610,200,160,210,frame);
			g.drawImage(slot[ran3],900,200,160,210,frame);
			g.setFont(new Font("Selif",Font.PLAIN,40));
			g.setColor(Color.BLACK);
			g.drawString("1で停止",310,470);
			g.drawString("2で停止",620,470);
			g.drawString("3で停止",910,470);

			g.setColor(Color.BLACK);
			g.setFont(new Font("Selif",Font.PLAIN,40));

			if((ran1==1&&ran2==1&&ran3==1)||(ran1==2&&ran2==2&&ran3==2)||(ran1==3&&ran2==3&&ran3==3)||
					(ran1==4&&ran2==4&&ran3==4)||(ran1==5&&ran2==5&&ran3==5)||
					(ran1==6&&ran2==6&&ran3==6)||(ran1==8&&ran2==8&&ran3==8)||
					(ran1==9&&ran2==9&&ran3==9)){
				//3つ揃ったら+30
				if(waittimer == 30){
					slot_pointn = 30;
					slot_pointn_sum += 30;
				}
				g.drawString("（ゾロ目！）",850,580);
			}
			else if((ran1==1||ran1==3||ran1==5||ran1==9)&&(ran2==1||ran2==3||ran2==5||ran2==9)
					&&(ran3==1||ran3==3||ran3==5||ran3==9)){
				//赤だけだったら+10
				if(waittimer == 30){
					slot_pointn = 10;
					slot_pointn_sum += 10;
				}
				g.drawString("（赤のみ）",850,580);
			}
			else if((ran1==2||ran1==4||ran1==6||ran1==8)&&(ran2==2||ran2==4||ran2==6||ran2==8)
			&&(ran3==2||ran3==4||ran3==6||ran3==8)){
				//青だけだったら+10
				if(waittimer == 30){
					slot_pointn = 10;
					slot_pointn_sum += 10;
				}
				g.drawString("（青のみ）",850,580);
			}
			else if(ran1==7&&ran2==7&&ran3==7){
				//7が揃ったら+60
				if(waittimer == 30){
					slot_pointn = 60;
					slot_pointn_sum += 60;
				}
				g.drawString("（777！！）",850,580);
			}
			else if(ran1==0&&ran2==0&&ran3==0){
				//ダイヤが揃ったら
				if(waittimer == 30){
					slot_pointn = 50;
					slot_pointn_sum += 50;
				}
				g.drawString("（ダイヤ揃い！）",850,580);
			}
			else if((ran1==0&&ran2==0)||(ran2==0&&ran3==0)||
					(ran1==0&&ran3==0)){
				//ダイヤが2つ揃ったら+15
				if(waittimer == 30){
					slot_pointn = 15;
					slot_pointn_sum += 15;
				}
				g.drawString("（ダイヤ2個！）",850,580);
			}
			else if(ran1==0||ran2==0||ran3==0){
				//ダイヤが1つでもあったら+5
				if(waittimer == 30){
					slot_pointn = 5;
					slot_pointn_sum += 5;
				}
				g.drawString("（ダイヤ1つ）",850,580);
			}

			slot_points = String.valueOf(slot_pointn);
		    drawStringCenter("+ "+slot_points+"ポイント",580,g);
			waittimer--;
			if(waittimer < 0){

				if(slot_play == 3){
					if(count > 2){
						enter_state = 2;
						slot_state = 2;
					}else{
						slot_string = 1;
						a1 = a2 = a3 = 0;
						enter_state = 0;
						slot_pointn = 0;
						slot_state = 0;
					}
				}

				if(slot_play == 5){
					if(count > 4){
						enter_state = 2;
						slot_state = 2;
					}else{
						slot_string = 1;
						a1 = a2 = a3 = 0;
						enter_state = 0;
						slot_pointn = 0;
						slot_state = 0;
					}
				}
				if(slot_play == 10){
					if(count > 9){
						enter_state = 2;
						slot_state = 2;
					}else{
						slot_string = 1;
						a1 = a2 = a3 = 0;
						enter_state = 0;
						slot_pointn = 0;
						slot_state = 0;
					}
				}
			}
		}
		public void SLOT_GAME_END(Graphics g){
			clear(g);
			g.setColor(new Color(240,230,140));
			g.fillRect(0,0,w,h);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Selif",Font.PLAIN,40));
			slot_pointn_sums = String.valueOf(slot_pointn_sum);
			g.drawString("あなたの合計は "+slot_pointn_sums+"ポイントでした。",300,300);

			if(slot_pointn_max < slot_pointn_sum) slot_pointn_max = slot_pointn_sum;
			drawStringCenter("本日の最高ポイント :"+slot_pointn_max+"ポイント",400,g);
		}


		public void SETTING(Graphics g){
			switch(setting_state){
			case 0:
				g.setColor(Color.BLACK);
				drawStringCenter("設定画面",100,g);
				g.drawString("Press 1 : ゲームを強制終了します.",200,160);
				g.drawString("Press 2 : セーブデータを初期化します(データのリセット).",200,200);
				g.drawString("Press 3 : セーブデータを表示します.",200,240);
				g.drawString("Press 4 : 隠しゲーム「星座占い」を起動します.",200,280);
				break;
			case 1:
				drawStringCenter("本当にゲームを終了しますか？(エンターで終了、バックで戻る)",100,g);
				break;
			case 2:
				System.exit(0);
				break;
			case 3:
				drawStringCenter("本当にセーブデータをリセットしますか？(エンターでリセット、バックで戻る)",100,g);
				break;
			case 4:
		     	waittimer--;
				drawStringCenter("リセットしました.",100,g);
				if(waittimer < 0) setting_state = 0;
				break;
			case 5:
				try{
					BufferedReader br = new BufferedReader(new FileReader("Uranai_savedata.txt"));

					String hl_3_1s = br.readLine();
					String hl_3_2s = br.readLine();
					String hl_3_3s = br.readLine();

					String hl_5_1s = br.readLine();
					String hl_5_2s = br.readLine();
					String hl_5_3s = br.readLine();
					String hl_5_4s = br.readLine();

					String hl_10_1s = br.readLine();
					String hl_10_2s = br.readLine();
					String hl_10_3s = br.readLine();
					String hl_10_4s = br.readLine();
					String hl_10_5s = br.readLine();

					String slot_pointn_maxs = br.readLine();

					String players = br.readLine();

					String mikuji_players = br.readLine();
					String seiza_players = br.readLine();
					String hl_players = br.readLine();
					String slot_players = br.readLine();

					String seiza_days = br.readLine();

					g.drawString("hl_3_1  :"+hl_3_1s,200,160);
					g.drawString("hl_3_2  :"+hl_3_2s,200,180);
				    g.drawString("hl_3_3  :"+hl_3_3s,200,200);
					g.drawString("hl_5_1  :"+hl_5_1s,200,220);
					g.drawString("hl_5_2  :"+hl_5_2s,200,240);
				    g.drawString("hl_5_3  :"+hl_5_3s,200,260);
				    g.drawString("hl_5_4  :"+hl_5_4s,200,280);
					g.drawString("hl_10_1 :"+hl_10_1s,200,300);
					g.drawString("hl_10_2 :"+hl_10_2s,200,320);
					g.drawString("hl_10_3 :"+hl_10_3s,200,340);
					g.drawString("hl_10_4 :"+hl_10_4s,200,360);
					g.drawString("hl_10_5 :"+hl_10_5s,200,380);
					g.drawString("slot_point_max:"+slot_pointn_maxs,200,400);
					g.drawString("プレイ人数  :"+players,200,420);
					g.drawString("(mi:"+mikuji_players+" ,seiza:"+seiza_players+
							" ,hl:"+hl_players+" ,slot:"+slot_players+")",300,420);

					g.drawString("←で1、→で2に変更。エンターで設定を反映します。",900,130);
					g.drawString("seiza_day :11/ "+seiza_days,1000,150);

					br.close();
				}catch(IOException e){
					System.out.println("入出力エラー");
				}
				g.drawString("エンターで更新、バックで戻る.",200,600);
				break;
			}
		}
	}

	public static void main(String[] args) throws InterruptedException{
		//注意！PCの解像度を変える場合、文字列の文字の大きさ、及びSEIZA_GAME(),HL_GAME()
		//内の各画像の位置の値も一緒に適宜変えること。

		//S(Shift+S)を11回以上連打すると設定画面に行ける。
		Uranai u = new Uranai(w,h,"占いゲーム");
	}
}
