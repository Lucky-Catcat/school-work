#include <SFML/Audio.hpp>
#include <SFML/Graphics.hpp>
#include<time.h>
#include<stdlib.h>
using namespace sf;

#define N         500		  //开屏大小(宽) 
#define M         899		  //开屏大小(长)
#define GridNum	  9          //游戏图片数目
#define COL       7          //游戏区列数
#define ROW       12           //游戏区行数
#define IMAGEW	  50          //游戏图片的长
#define IMAGEH	  45          //游戏图片的宽
#define leftedge  74         //游戏区距左边框距离
#define topedge   264         //游戏区距上边框距离
#define lefteillu 44          //说明区据左边框距离
#define topeillu  31         //说明区据距上边框距离
#define ILLUW     88          //说明区的长
#define ILLUH     27          //说明区的宽

Vector2i mouse;//鼠标信息
RectangleShape box1;//方框
RectangleShape box2;
RectangleShape box3;
int   GridID[ROW + 2][COL + 2];  //游戏图纸坐标
int click = 0;//相邻单击次数
bool isMoving = false;
bool isSwap = false;
bool  isIllustrate = false;//说明按钮

struct Block {
	int x, y;//坐标值
	int row, col;//第几行，第几列
	int kind=-1;//表示方块种类
	bool match=false;//表示是否成三
	int alpha=255;//透明度
}image[ROW+2][COL+2];

struct clickfor {       //记录击中图片信息
	int idx, idy;      //图纸坐标
	int kindid;       //图片类型
}one,two;

bool Single_click_judge(int mousex,int mousey);//判断单击是否有效
void Leftbottondown(Vector2i mouse);//左击时的变化
void Load_picture(Sprite* sprite, RenderWindow* window);//绘制方格图片
void swap(Block a, Block b);//交换方格
void rid();//消除方块
void TranstDracoor(int mousex, int mousey, int* idx, int* idy);//转化为图纸坐标
bool Judg_val(int leftx, int lefty);//判断鼠标是否在游戏区内
void Draw_frame_coor(int leftx, int lefty);//游戏区方框绘制
void GridPhy_coor(int* leftx, int* lefty);//游戏区物理坐标转化
void GridPhy_illu(int* leftx, int* lefty);//说明区物理坐标转化
void Draw_box(int leftx, int lefty,int w,int h);//方框绘制
void Draw_frame_illu(int leftx, int lefty);//说明区方框绘制
void Open_illustrate(Vector2i mouse);//打开说明书
void SeleReact(int leftx, int lefty);//显示击中效果
void RecordInfor(int leftx, int lefty, clickfor* grid);//记录击中图片信息
void inspect();//检查匹配
void domoving();//移动处理
void initGrid();//图片加载
void doevent(RenderWindow* window);//事件处理
void revert();//还原
void update();//更新表格

int main()
{
	srand((unsigned)time(NULL));
	RenderWindow window(VideoMode(N, M), "xiaoxiaole!");
	window.setFramerateLimit(60);
	Music music;
	if (!music.openFromFile("bgm.flac"))//加载音乐
		return -1;
	music.play();
	music.setLoop(true);//循环播放音乐
	Texture t1, t2;
	t1.loadFromFile("picture/bg2.gif");//背景图片加载
	t2.loadFromFile("picture/bg1.gif");//游戏图片加载
	Sprite spriteBg(t1);
	Sprite spriteBlock(t2);
	initGrid();//加载游戏图片
	while (window.isOpen())
	{
		doevent(&window);//事件处理
		domoving();//移动
		inspect();//检查匹配
		if (!isMoving) 
			rid();//消除
		revert();//还原处理
		if (!isMoving)
			update();//更新表格
		window.draw(spriteBg);//绘制游戏背景
		Load_picture(&spriteBlock, &window);//绘制方格图片
		window.draw(box1);//绘制方框
		window.draw(box2);
		window.draw(box3);
		window.display();//显示画面
	}
	return 0;
}


void initGrid() {
	for (int i = 1; i <= ROW; i++) {
		for (int j = 1; j <= COL; j++) {
			image[i][j].kind = rand() % GridNum;
			image[i][j].col = j;
			image[i][j].row = i;
			image[i][j].x = j * IMAGEW;
			image[i][j].y = i * IMAGEH;
		}
	}
}

void Load_picture(Sprite * sprite,RenderWindow *window)
{
	for (int i = 1; i <= ROW; i++) {
		for (int j = 1; j <= COL; j++) {
			Block p = image[i][j];
			sprite->setTextureRect(
				IntRect(p.kind * IMAGEW, 0, IMAGEW, IMAGEH));
			sprite->setColor(Color(255, 255, 255, p.alpha));
			sprite->setPosition(p.x, p.y);
			sprite->move(leftedge-IMAGEW,topedge-IMAGEH);
			window->draw(*sprite);
		}
	}
}

void doevent(RenderWindow* window) {
	Event event;
	while (window->pollEvent(event)) {
		if (event.type == Event::Closed)
			window->close();
	}
	if (event.type == Event::MouseButtonPressed) {
		if (event.key.code == Mouse::Left) {
			mouse = Mouse::getPosition(*window);
			if (Single_click_judge(mouse.x, mouse.y))//判断单击是否有效
			{
				Leftbottondown(mouse);//左击时的变化
			}
			else if (isIllustrate) {
				Draw_frame_illu(mouse.x, mouse.y);
				Open_illustrate(mouse);
			}
		}
	}
}

bool Single_click_judge(int mousex,int mousey)
{
	int idx, idy;
	TranstDracoor(mousex, mousey, &idx, &idy);
	if (Judg_val(mouse.x, mouse.y))
		return true;
	return false;
}

void Leftbottondown(Vector2i mouse) {
	int posx, posy;
	if (!isSwap && !isMoving) 
		click++;
	if (click == 1) {
		SeleReact(mouse.x, mouse.y);
		RecordInfor(mouse.x, mouse.y, &one);
	}
	else if (click == 2) {
		TranstDracoor(mouse.x, mouse.y, &posx, &posy);
		if ((posx != one.idx) || (posy != one.idy)) {
			RecordInfor(mouse.x, mouse.y, &two);
			if (abs(two.idx - one.idx) + abs(two.idy - one.idy) == 1) {
				swap(image[one.idy][one.idx], image[two.idy][two.idx]);
				isSwap = true;
			}
			else {
				click = 1;
				SeleReact(mouse.x, mouse.y);
				RecordInfor(mouse.x, mouse.y, &one);
			}
		}
		else {
			click = 1;
			SeleReact(mouse.x, mouse.y);
			RecordInfor(mouse.x, mouse.y, &one);
		}
		click = 0;
	}
}

void swap(Block a, Block b) {
	std::swap(a.col, b.col);
	std::swap(a.row, b.row);
	image[a.row][a.col] = a;
	image[b.row][b.col] = b;
}

void rid() {
	for (int i = 1; i <= ROW; i++) {
		for (int j = 1; j <= COL; j++) {
			if (image[i][j].match && image[i][j].alpha > 5) {
				image[i][j].alpha -= 5;
			}
		}
	}
}

void TranstDracoor(int mousex, int mousey, int* idx, int* idy) {
	if (Judg_val(mousex, mousey)) {
		*idx = (mousex - leftedge) / IMAGEW +1;
		*idy = (mousey - topedge) / IMAGEH +1;
	}
}

bool Judg_val(int leftx, int lefty) {
	if ((leftx > lefteillu) && (leftx<lefteillu + ILLUW) &&
		(lefty>topeillu) && (lefty < topeillu + ILLUH))
		isIllustrate = true;
	return ((leftx >leftedge) && (leftx<leftedge + IMAGEW * COL) &&
		(lefty>topedge) && (lefty < topedge + IMAGEH * ROW));
}

void Draw_frame_coor(int leftx, int lefty){
	int x = leftx, y = lefty;
		GridPhy_coor(&x, &y);
		Draw_box(x, y,IMAGEW,IMAGEH);
}

void Draw_frame_illu(int leftx, int lefty) {
	int x = leftx, y = lefty;
	GridPhy_illu(&x, &y);
	Draw_box(x, y, ILLUW, ILLUH);
}

void GridPhy_coor(int* leftx, int* lefty)			//转化为标准物理坐标
{
	*leftx = ((*leftx - leftedge) / IMAGEW) * IMAGEW + leftedge;
	*lefty = ((*lefty - topedge) / IMAGEH) * IMAGEH + topedge;
}

void GridPhy_illu(int* leftx, int* lefty) {
	*leftx = ((*leftx - lefteillu) / ILLUW) * ILLUW + lefteillu;
	*lefty = ((*lefty - topeillu) / ILLUH) * ILLUH + topeillu;
}

void Draw_box(int leftx, int lefty,int w,int h) {
	box1.setSize(Vector2f(w-1, h-1));
	box1.setOutlineColor(Color(126,91,68,255));
	box1.setOutlineThickness(1);
	box1.setFillColor(Color(0, 0, 0, 0));
	box1.setPosition(leftx, lefty);

	box2.setSize(Vector2f(w-5, h-5));
	box2.setOutlineColor(Color(126,91,68,255));
	box2.setOutlineThickness(1);
	box2.setFillColor(Color(0, 0, 0, 0));
	box2.setPosition(leftx+2, lefty+2);

	box3.setSize(Vector2f(w-2, h-2));
	box3.setOutlineColor(Color(250,230,169,255));
	box3.setOutlineThickness(1);
	box3.setFillColor(Color(0, 0, 0, 0));
	box3.setPosition(leftx+1, lefty+1);
}

void Open_illustrate(Vector2i mouse) {
	if (!isSwap && !isMoving)
		click++;
	if (click == 1) {
		Draw_frame_illu(mouse.x, mouse.y);
	}
	else if (click == 2) {
		RenderWindow windows(VideoMode(400,696), "Illustrate.");
		Texture t3;
		t3.loadFromFile("picture/bg3.gif");//游戏说明加载
		Sprite spriteIllu(t3);
		while (windows.isOpen()) {
			Event e;
			while (windows.pollEvent(e)) {
				if (e.type == Event::Closed)
					windows.close();
				isIllustrate = false;
			}
			windows.draw(spriteIllu);
			windows.display();
		}
		click = 0;
	}
}

void SeleReact(int leftx, int lefty) {
	if (Judg_val(leftx, lefty)) 
		Draw_frame_coor(leftx, lefty);
}

void RecordInfor(int leftx, int lefty, clickfor* grid) {
	TranstDracoor(leftx, lefty, &(*grid).idx, &(*grid).idy);
	(*grid).kindid = image[(*grid).idx][(*grid).idy].kind;
}

void inspect() {
	for (int i = 1; i <= ROW; i++) {
		for (int j = 1; j <= COL; j++) {
			if (image[i][j].kind == image[i - 1][j].kind &&
				image[i][j].kind == image[i + 1][j].kind) 
				for (int m = -1; m <= 1; m++) image[i + m][j].match++;

			if(image[i][j].kind==image[i][j-1].kind&&
				image[i][j].kind==image[i][j+1].kind)
				for(int m=-1;m<=1;m++) image[i][j+m].match++;
		}
	}
}

void domoving() {
	int dx, dy;
	isMoving = false;
	for (int i = 1; i <= ROW; i++) {
		for (int j = 1; j <= COL; j++) {
			Block& p = image[i][j];
			dx = p.x - p.col * IMAGEW;
			dy = p.y - p.row * IMAGEH;
			for (int k = 0; k < 5; k++) {
				if (dx != 0) p.x -=  dx / abs(dx);
				if (dy != 0) p.y -=  dy / abs(dy);
			}
			if (dx != 0 || dy != 0) 
				isMoving = true;
		}
	}
}

void revert() {
	if (isSwap && !isMoving) {
		int score = 0;
		for(int i=1;i<=ROW;i++){
			for (int j = 1; j <= COL; j++) {
				score += image[i][j].match;
			}
		}
		if (score == 0) 
			swap(image[one.idy][one.idx], image[two.idy][two.idx]);
		isSwap = false;
	}
}

void update() {
	for (int i = ROW; i > 0; i--) {
		for (int j = 1; j <= COL; j++) {
			if (image[i][j].match) {
				for (int k = i - 1; k > 0; k--) {
					if (image[k][j].match == 0) {
						swap(image[i][j], image[k][j]);
						break;}}}}}
	for (int j = 1; j <= COL; j++) {
		int n = 0;
		for (int i = ROW; i > 0; i--) {
			if (image[i][j].match) {
				image[i][j].kind = rand() % GridNum;
				image[i][j].y = -IMAGEH * n;
				n++;
				image[i][j].match = false;
				image[i][j].alpha = 255;}}}
}
