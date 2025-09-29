#include "parse.h"
#include <string.h>
#include <ctype.h>
#include <stdlib.h>

static void trim(char *s){
    char *p=s;
    while(isspace((unsigned char)*p))p++;
    if(p!=s)memmove(s,p,strlen(p)+1);
    size_t len=strlen(s);
    while(len>0&&isspace((unsigned char)s[len-1]))s[--len]='\0';
}

int parse_player(const char *s, Player *p){
    char tmp[64]; strncpy(tmp,s,63); tmp[63]='\0'; trim(tmp);
    char *sp = strchr(tmp, ' ');
    if (!sp) {
        return 0;
    }
    *sp = '\0';
    char *type=tmp, *color=sp+1; trim(type); trim(color);
    for(char *q=type;*q;q++)*q=tolower(*q);
    for(char *q=color;*q;q++)*q=toupper(*q);
    if(strcmp(type,"user")==0)p->type=PLAYER_USER;
    else if(strcmp(type,"comp")==0)p->type=PLAYER_COMP;
    else return 0;
    if(strlen(color)!=1||(color[0]!='W'&&color[0]!='B')) return 0;
    p->color=color[0]; return 1;
}

int parse_game_command(char *args, Game *g){
    char *c1=strchr(args,','); if(!c1) return 0; *c1='\0';
    char *nstr=args; int N=atoi(nstr); if(N<=2) return 0;
    char *rest=c1+1; char *c2=strchr(rest,','); if(!c2) return 0; *c2='\0';
    char *p1s=rest,*p2s=c2+1;
    Player p1,p2;
    if(!parse_player(p1s,&p1)||!parse_player(p2s,&p2)) return 0;
    if(p1.color==p2.color) return 0;
    return game_start(g,N,p1,p2);
}
