package com.advoa.sparkplugin;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JWindow;

public class OAAlertToolTip {

	private AdvOAPreferences preferences;
	// ������ʾ��
	private int _width = 220;

	// ������ʾ��
	private int _height = 140;

	// �趨ѭ���Ĳ���
	private int _step = 30;

	// ÿ��ʱ��
	private int _stepTime = 30;

	// ��ʾʱ��
	private int _displayTime = 20000;

	// Ŀǰ�����������ʾ����
	private int _countOfToolTip = 0;

	// ��ǰ���������
	private int _maxToolTip = 0;

	// ����Ļ����ʾ�����������ʾ����
	private int _maxToolTipSceen;

	// ����
	private Font _font;

	// �߿���ɫ
	private Color _bgColor;

	// ������ɫ
	private Color _border;

	// ��Ϣ��ɫ
	private Color _messageColor;

	// ��ֵ�趨
	int _gap;

	// �Ƿ�Ҫ��������jre1.5���ϰ汾����ִ�У�
	boolean _useTop = true;

	/**
	 * ���캯������ʼ��Ĭ��������ʾ����
	 * 
	 */
	public OAAlertToolTip() {
		// �趨����
		_font = new Font("����", 0, 18);
		// �趨�߿���ɫ
		_bgColor = new Color(241, 241, 229);
		_border = Color.BLUE;
		_messageColor = Color.BLUE;
		_useTop = true;

	}

	/**
	 * �ع�JWindow������ʾ��һ������ʾ��
	 * 
	 */
	class ToolTipSingle extends JWindow {
		private static final long serialVersionUID = 1L;

		private Animation animation = new Animation(this);
		private ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/ttclose.png"));
		private JLabel _label = new JLabel(icon);
		private JButton _button = new JButton("�������OA");
		private JTextArea _message = new JTextArea();

		public ToolTipSingle() {
			initComponents();
		}

		private void initComponents() {
			setSize(_width, _height);
			preferences = new AdvOAPreferences();
			_message.setFont(getMessageFont());

			ImagePanel externalPanel = new ImagePanel();
			externalPanel.setLayout(null);

			externalPanel.add(_message);

			externalPanel.add(_button);

			externalPanel.add(_label);
			_message.setBounds(10, 50, 200, 50);
			_button.setBounds(65, 105, 100, 25);
			_label.setBounds(200, 5, 16, 16);
			_button.setVisible(preferences.getButtonCheck());
			_message.setBackground(_bgColor);

			_message.setLineWrap(true);
			_message.setWrapStyleWord(true);

			_message.setForeground(getMessageColor());

			getContentPane().add(externalPanel);

			_label.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					_label.setToolTipText("�ر�");

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					animation.close();

				}
			});

			_button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					XmlUtil util = new XmlUtil();
					String url = util.getRealURL(util.getXMLText(
							util.getList("//root/oaalert/servers/oaserver"),
							preferences.getServerSelection(),
							preferences.getLoginSelection()), preferences);
					toOA(url);
				}
			});
		}

		public void toOA(String url) {
			try {
				Process ps = null;
				Runtime rn = Runtime.getRuntime();
				ps = rn.exec("reg query HKEY_CLASSES_ROOT\\Applications\\iexplore.exe\\shell\\open\\command");
				ps.getOutputStream().close();
				InputStreamReader i = new InputStreamReader(ps.getInputStream());
				String line;
				BufferedReader ir = new BufferedReader(i);
				while ((line = ir.readLine()) != null) {
					if (line.contains("\"")) {
						String[] str = line.split("\"");
						rn.exec(str[1] + " " + url);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * ������ʼ
		 * 
		 */
		public void animate() {
			animation.start();
		}

	}

	/**
	 * ���ദ�򶯻�����
	 * 
	 */
	class Animation extends Thread {

		ToolTipSingle _single;

		public Animation(ToolTipSingle single) {
			this._single = single;
		}

		/**
		 * ���ö���Ч�����ƶ���������
		 * 
		 * @param posx
		 * @param startY
		 * @param endY
		 * @throws InterruptedException
		 */
		private void animateVertically(int posx, int startY, int endY)
				throws InterruptedException {
			_single.setLocation(posx, startY);
			if (endY < startY) {
				for (int i = startY; i > endY; i -= _step) {
					_single.setLocation(posx, i);
					Thread.sleep(_stepTime);
				}
			} else {
				for (int i = startY; i < endY; i += _step) {
					_single.setLocation(posx, i);
					Thread.sleep(_stepTime);
				}
			}
			_single.setLocation(posx, endY);
		}

		/**
		 * ��ʼ��������
		 */
		public void run() {
			try {
				boolean animate = true;
				GraphicsEnvironment ge = GraphicsEnvironment
						.getLocalGraphicsEnvironment();
				Rectangle screenRect = ge.getMaximumWindowBounds();
				int screenHeight = (int) screenRect.height;
				int startYPosition;
				int stopYPosition;
				if (screenRect.y > 0) {
					animate = false;
				}
				_maxToolTipSceen = screenHeight / _height;
				int posx = (int) screenRect.width - _width - 1;
				_single.setLocation(posx, screenHeight);
				_single.setVisible(true);
				if (_useTop) {
					_single.setAlwaysOnTop(true);
				}
				if (animate) {
					startYPosition = screenHeight;
					stopYPosition = startYPosition - _height - 1;
					if (_countOfToolTip > 0) {
						stopYPosition = stopYPosition
								- (_maxToolTip % _maxToolTipSceen * _height);
					} else {
						_maxToolTip = 0;
					}
				} else {
					startYPosition = screenRect.y - _height;
					stopYPosition = screenRect.y;

					if (_countOfToolTip > 0) {
						stopYPosition = stopYPosition
								+ (_maxToolTip % _maxToolTipSceen * _height);
					} else {
						_maxToolTip = 0;
					}
				}

				_countOfToolTip++;
				_maxToolTip++;

				animateVertically(posx, startYPosition, stopYPosition);
				Thread.sleep(_displayTime);
				animateVertically(posx, stopYPosition, startYPosition);

				_countOfToolTip--;
				_single.setVisible(false);
				_single.dispose();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public void close() {
			_single.dispose();
		}
	}

	/**
	 * �趨��ʾ��ͼƬ����Ϣ
	 * 
	 * @param icon
	 * @param msg
	 */
	public void setToolTip(String msg) {
		ToolTipSingle single = new ToolTipSingle();
		single._message.setText(msg);
		single.animate();
	}

	/**
	 * ��õ�ǰ��Ϣ����
	 * 
	 * @return
	 */
	public Font getMessageFont() {
		return _font;
	}

	/**
	 * ���õ�ǰ��Ϣ����
	 * 
	 * @param font
	 */
	public void setMessageFont(Font font) {
		_font = font;
	}

	/**
	 * ��ñ߿���ɫ
	 * 
	 * @return
	 */
	public Color getBorderColor() {
		return _border;
	}

	/**
	 * ���ñ߿���ɫ
	 * 
	 * @param _bgColor
	 */
	public void setBorderColor(Color borderColor) {
		this._border = borderColor;
	}

	/**
	 * �����ʾʱ��
	 * 
	 * @return
	 */
	public int getDisplayTime() {
		return _displayTime;
	}

	/**
	 * ������ʾʱ��
	 * 
	 * @param displayTime
	 */
	public void setDisplayTime(int displayTime) {
		this._displayTime = displayTime;
	}

	/**
	 * ��ò�ֵ
	 * 
	 * @return
	 */
	public int getGap() {
		return _gap;
	}

	/**
	 * �趨��ֵ
	 * 
	 * @param gap
	 */
	public void setGap(int gap) {
		this._gap = gap;
	}

	/**
	 * �����Ϣ��ɫ
	 * 
	 * @return
	 */
	public Color getMessageColor() {
		return _messageColor;
	}

	/**
	 * �趨��Ϣ��ɫ
	 * 
	 * @param messageColor
	 */
	public void setMessageColor(Color messageColor) {
		this._messageColor = messageColor;
	}

	/**
	 * ���ѭ������
	 * 
	 * @return
	 */
	public int getStep() {
		return _step;
	}

	/**
	 * �趨ѭ������
	 * 
	 * @param _step
	 */
	public void setStep(int _step) {
		this._step = _step;
	}

	public int getStepTime() {
		return _stepTime;
	}

	public void setStepTime(int _stepTime) {
		this._stepTime = _stepTime;
	}

	public Color getBackgroundColor() {
		return _bgColor;
	}

	public void setBackgroundColor(Color bgColor) {
		this._bgColor = bgColor;
	}

	public int getHeight() {
		return _height;
	}

	public void setHeight(int height) {
		this._height = height;
	}

	public int getWidth() {
		return _width;
	}

	public void setWidth(int width) {
		this._width = width;
	}

}