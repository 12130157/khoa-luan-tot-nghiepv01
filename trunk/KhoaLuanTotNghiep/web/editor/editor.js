-/*
	Product : SN Editor, trình soan thao van ban  WYSIWYG cho cac ung dung web ;
	Author : Sacroyant Nguyen ; Email : saroyant@gmail.com  ; Homepage : http://sacroyant.googlepages.com 
*/
function getNavigator(){var bUserAgent=navigator.userAgent.toLowerCase();if(bUserAgent.indexOf('msie')>0){return 'msie'}else if(bUserAgent.indexOf('opera')==0){return 'opera'}else if(bUserAgent.indexOf('safari')>0){if(bUserAgent.indexOf('chrome')==-1){return 'safari'}else{return 'chrome'}}else{return 'firefox'}};
var nav=getNavigator();
var isIE=(nav=='msie')?true:false;
var isMz=(nav=='firefox')?true:false;
var isOp=(nav=='opera')?true:false;
var isGC=(nav=='chrome')?true:false;
var isSa=(nav=='safari')?true:false;

function Editor(instanceName, contain, defaultContent, width, height){
  this.is=instanceName;
  this.contain=contain;
  this.ID=this.is+'_editor';
  this.UI=null;
  this.ed=null;
  this.cnt=defaultContent;
  this.width=width<300?300:width;
  this.height=height<200?200:height;
  this.cache='';
  this.submenu=null;
  this.btnApp=null;
  this.preilta=null;
  this.ToolBar=contain+'_toolBar';
  this.sm=function(el,s){this.cm();this.rel(this.is+'_'+el).style.position='relative';var paf=document.createElement('div');var pop=document.createElement('div');var w=this.width;var h=this.height;paf.id=this.is+'_ePaf';pop.id=this.is+'_'+el+'_pop';with(paf.style){position='absolute';padding='0px';backgroundImage='url("'+this.iconPath+'bg.gif")';top=isOp?'35px':'28px';left='0px';zIndex='2';width=(w==0)?'100%':w+'px';height=(h-58)+'px';}with(pop.style){position='absolute';padding='2px';borderWidth='1px';borderStyle='solid';borderColor='#cccccc';background='#ffffff';cursor='default';top=isOp?'38px':'25px';left='0px';zIndex='3';}pop.innerHTML=s;this.rel(this.is+'_subMenu').appendChild(paf);this.rel(this.is+'_'+el+'_menu').appendChild(pop);this.sa(this.is+'_ePaf', 'click', this.is+'.cm()');this.submenu=this.is+'_'+el+'_pop';this.btnApp=this.is+'_'+el+'_menu';this.preilta=paf;};this.addButton=function(id,t,ic,f,no,i){if(i!=null&&!isNaN(i)&&i>=0&&i<this.bs.length){this.bs.splice(i,0,new Array(id,t,ic,f,(no==null)?1:no));}else{this.bs.push(new Array(id,t,ic,f,(no==null)?1:no));}this.ltb()};this.moveButton=function(id,ix,r){for(var i=0;i<this.bs.length;i++){if(id==this.bs[i][0]){if(r==2){this.bs[i][4]=2}else{this.bs[i][4]=1}if(!isNaN(ix)&&ix>=0&&ix<this.bs.length){var t=this.bs[i];this.bs.splice(i,1);this.bs.splice(ix,0,t);};break;}}this.ltb();};this.cm=function(){if(this.submenu!=null){this.clearTxt(this.btnApp);this.submenu=null;if(this.preilta){this.rel(this.is+'_subMenu').removeChild(this.preilta);this.preilta=null;}}};this.unformat=function(){if(isOp){var sel=this.UI.getSelection();this.fmt('inserthtml',sel);}else{this.fmt('unlink');this.fmt('removeformat');}};this.getSelectedText=function(){if(!isIE){var sel=this.UI.getSelection();if(sel==''){return ''}var rg=sel.getRangeAt(0);var doc=rg.extractContents();var xmls=new XMLSerializer();return xmls.serializeToString(doc)}else{var ir=this.ed.selection.createRange();ir.select();return ir.htmlText;}};this.quote=function(){try{var s=this.getSelectedText();if(s!=''){var qt='';var sq='<br><blockquote style="border-left:1px solid rgb(204, 204, 204); margin: 1px 1px 1px 20px; padding-left: 8px;">',eq='</blockquote><br>';if(!isIE){qt=sq+s+eq;this.fmt('inserthtml',qt);}else{var ig=this.ed.selection.createRange();ig.select();qt=sq+s+eq;ig.pasteHTML(qt);}}}catch(e){}};this.code=function(){try{this.unformat();var ss=this.getSelectedText();if(ss!=''){var sc='<br>'+this.openCodeTag+'<br>', ec='<br>'+this.closeCodeTag+'<br>';if(!isIE){this.fmt('inserthtml',sc+ss+ec);}else{var ig=this.ed.selection.createRange();ig.select();ig.pasteHTML(sc+ss+ec);}}}catch(e){}};this.rel=function(ob){try{return document.getElementById(ob);}catch(e){}};this.gTxt=function(ob,txt){try{this.rel(ob).innerHTML=txt;}catch(e){}};this.clearTxt=function(ob){try{this.gTxt(ob,"");}catch(e){}};this.setStyle=function(ob,cln){this.rel(ob).className=cln;};this.osl=function(){var s='<table border="0" width="120" height="100%" cellpadding="0" cellspacing="0">';var h=0,k=0;for(var i=0;i<8;i++){s+='<tr height="20" >';h=k;for(var j=0;j<4;j++){if(this.emoticons[(h+j)]){s+='<td align="center" width="30" height="25" title="'+this.emoticons[(h+j)][0]+'"><img src="'+this.emoticonPath+this.emoticons[(h+j)][2]+'" onclick="'+this.is+'.insertImage(\''+this.emoticonPath+this.emoticons[(h+j)][2]+'\');"></td>';}else{s+='<td>&nbsp;</td>';}k++;}s+='</tr>';}s+='</table>';this.sm('btnEmoticon',s);};this.cl=function(no){var s='<div><table border="0" bgcolor="#999999" cellspacing="0" cellpadding="0">';var m=0;for(var i=0;i<5;i++){s+='<tr>';for(var j=0;j<8;j++){s+='<td title="'+this.colors[m][1]+'"><'+(isIE?'button':'div')+' id="bcm_'+m+'_'+no+'" class="cOut" onmouseover="'+this.is+'.setStyle(\'bcm_'+m+'_'+no+'\',\'cOver\');" onmouseout="'+this.is+'.setStyle(\'bcm_'+m+'_'+no+'\',\'cOut\');" style="background-color:'+this.colors[m][0]+';" onclick="'+this.is+'.fmt(\''+(no==1?'forecolor':(isIE?'backcolor':'hilitecolor'))+'\',\''+this.colors[m][0]+'\');'+this.is+'.cm();">&nbsp;</'+(isIE?'button':'div')+'></td>';m++;}s+='</tr>';}s+='</table></div>';this.sm((no==1?'btnFontcolor':'btnBackcolor'),s);};this.fsl=function(){var z=0,s='<table border="0" cellpadding="0" cellspacing="1" width="20">';for(var i=0;i<this.fontSize.length;i++){z=i+1;s+='<tr><td><'+(isIE?'button':'div')+' id="fsm'+i+'" class="mOut" style="border:none;font-family:serif;width:100%;text-align:center;margin:0px;padding:0px;"  onclick="'+this.is+'.fmt(\'fontsize\',\''+this.fontSize[i]+'\');'+this.is+'.cm();" onmouseover="'+this.is+'.setStyle(\'fsm'+i+'\',\'mOn\');" onmouseout="'+this.is+'.setStyle(\'fsm'+i+'\',\'mOut\');" title="'+z+'"><font size="'+this.fontSize[i]+'">'+this.fontSize[i]+'</font></'+(isIE?'button':'div')+'></td></tr>';}s+='</table>';this.sm('btnFontsize',s);};this.ffl=function(){var s='<table cellpadding="0" cellspacing="0" width="100">';for(var i=0;i<this.fontFace.length;i++){s+='<tr><td><'+(isIE?'button':'div')+' id="ffm'+i+'" class="mOut" style="border:none;font-family:serif;width:100%;text-align:left;margin:0px;padding:0px;" onclick="'+this.is+'.fmt(\'fontname\',\''+this.fontFace[i]+'\');'+this.is+'.cm();" onmouseover="'+this.is+'.setStyle(\'ffm'+i+'\',\'mOn\');" onmouseout="'+this.is+'.setStyle(\'ffm'+i+'\',\'mOut\');" title="'+this.fontFace[i]+'"> &nbsp; <font face="'+this.fontFace[i]+'">'+this.fontFace[i]+'</font></'+(isIE?'button':'div')+'></td></tr>';}s+='</table>';this.sm('btnFontface',s);};this.undoClear=function(){if(this.cache!=''){this.cnt=this.cache;this.ed.body.innerHTML=this.cnt;this.cache='';this.clearTxt('undo');}};this.clearAll=function(){this.cache=this.getHTML();this.cnt="";this.ed.body.innerHTML='<br>';if(this.cache.length>5){this.gTxt('undo','<span class="ctr" id="btnUndoClear" title="Undo Clear">Undo</span>');this.sa('btnUndoClear','click',this.is+'.undoClear();');}};this.selectAll=function(){this.cm();this.fmt('selectall');this.UI.focus();};this.getHTML=function(){return this.ed.body.innerHTML;};this.getContent=function(){return this.getHTML();};this.getText=function(){return isIE?this.ed.body.innerText:this.ed.body.textContent;};
  this.iconPath='../../pics/';
  this.emoticonPath='http://us.i1.yimg.com/us.yimg.com/i/mesg/emoticons7/';
  this.textColor='#000000';
  this.textFont='sans-serif';
  this.textSize='14px';
  this.backgroundImage=new Object();
  this.backgroundImage.url='../../pics/background.jpg';
  this.backgroundImage.repeat='no-repeat';
  this.backgroundImage.position='top right';
  this.backgroundColor='#ffffff';
  this.toolbarColor="#f2fff2";
  this.showFootbar=true;
  this.openCodeTag='[script]';
  this.closeCodeTag='[/script]';
  this.removeButton=function(btn){
      for(var k=0;k<arguments.length;k++){
          if(!isNaN(arguments[k])){
              if(arguments[k]>=0&&arguments[k]<this.bs.length){
                  this.bs.splice(arguments[k],1);
              }
          }else{
              for(var i=0;i<this.bs.length;i++){
                  if(arguments[k]==this.bs[i][0]){
                      this.bs.splice(i,1);
                      break;
                  }
              }
          }
      }
      this.ltb()
  };
  this.ltb=function(){
      var s='<table border="0" cellpadding="1" cellspacing="0">';
      var tt=1;
      for(var i=0;i<this.bs.length;i++){
          if(this.bs[i][4]!=1){
              tt=2;
              break;
          }
      }
      if(tt==1){
          s+='<tr height="25">';
          for(var i=0;i<this.bs.length;i++){
              s+='<td width="20">';
              s+='<span style="position:relative;" id="'
                  +this.is+'_'+this.bs[i][0]
                  +'_menu"></span><div id="'
                  +this.is+'_'+this.bs[i][0]
                  +'" style="background-image:url('
                  +this.iconPath+this.bs[i][2]
                  +');background-repeat:no-repeat;background-position: center center;" class="bOut" title="'
                  +this.bs[i][1]+'"></div>';
              s+='</td>';
          }
          s+='</tr>';
      }else if(tt==2){
          s+='<tr height="20">';
          var mx=m=n=0;
          r=[[],[]];
          for(var i=0;i<this.bs.length;i++){
              if(this.bs[i][4]==1){
                  r[0].push(this.bs[i]);
                  m++;
              }else{
                  r[1].push(this.bs[i]);
                  n++;
              }
          }
          mx=m>n?m:n;
          for(var i=0;i<mx;i++){
              if(i<r[0].length){s+='<td width="20">';
                  s+='<span style="position:relative;" id="'
                      +this.is+'_'+r[0][i][0]+'_menu"></span><div id="'
                      +this.is+'_'+r[0][i][0]+'" style="background-image:url('
                      +this.iconPath+r[0][i][2]
                      +');background-repeat:no-repeat;background-position: center center;" class="bOut" title="'
                      +r[0][i][1]+'"><img src="'
                      +this.iconPath+'bg.gif" width="18" height="18"></div>';
                  s+='</td>';
              }else{
                  s+='<td width="20">&nbsp;</td>';
              }
          }
          s+='</tr>';
          s+='<tr height="25">';
          for(var i=0;i<mx;i++){
              if(i<r[1].length){
                  s+='<td width="20">';
                  s+='<span style="position:relative;" id="'
                      +this.is+'_'+r[1][i][0]+'_menu"></span><div id="'
                      +this.is+'_'+r[1][i][0]+'" style="background-image:url('
                      +this.iconPath+r[1][i][2]
                      +');background-repeat:no-repeat;background-position: center center;" class="bOut" title="'
                      +r[1][i][1]+'"><img src="'
                      +this.iconPath+'bg.gif" width="18" height="18"></div>';
                  s+='</td>';
              }else{
                  s+='<td width="20">&nbsp;</td>';
              }
          }
          s+='</tr>';}s+='</table>';this.gTxt(this.ToolBar,s);for(var i=0;i<this.bs.length;i++){if(this.bs[i].callback!=''){this.sa(this.is+'_'+this.bs[i][0],'click',this.bs[i][3]);}this.sa(this.is+'_'+this.bs[i][0],'mouseout',this.is+'.setStyle("'+this.is+'_'+this.bs[i][0]+'","bOut")');this.sa(this.is+'_'+this.bs[i][0], 'mouseover', this.is+'.setStyle("'+this.is+'_'+this.bs[i][0]+'","bOver")');}};this.sa=function(o,e,f){var el=this.rel(o);if(el!=null){if(el.addEventListener){el.addEventListener(e,new Function(f),false)}else if(el.attachEvent){el.attachEvent('on'+e, new Function(f))}}};this.display=function(){var sEditor='<iframe id="'+this.ID+'" style="width:'+((this.width==0)?'99%':(this.width-5)+'px')+';height:'+(this.height-(this.showFootbar?55:30))+'px;overflow:auto;margin:0px;padding:0px;"></iframe>',sCB='',s='<table border="0" width="'+((this.width==0)?'100%':this.width)+'" height="'+this.height+'" cellpadding="1" cellspacing="0" style="background-color:#f7fff3;"><tr height="25" bgcolor="'+this.toolbarColor+'"><td colspan="2"><span id="'+this.is+'_subMenu" style="position:relative;top:0px;left:0px;"></span><span id="'+this.ToolBar+'">&nbsp;</span></td></tr><tr height="'+(this.height-(this.showFootbar?50:25))+'"><td colspan="2">'+sEditor+'</td></tr>';if(this.showFootbar){for(var i=0;i<this.controlButtons.length;i++){sCB+='<input type="button" name="'+this.is+'_'+this.controlButtons[i][0]+'" id="'+this.is+'_'+this.controlButtons[i][0]+'" value="'+this.controlButtons[i][1]+'" style="width:100px;" class="mouseOut" title="'+this.controlButtons[i][1]+'">';}s+='<tr height="25"><td colspan="2"><table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0"><tr><td width="30%">&nbsp; <span class="ctrE" title="Select All" onclick="'+this.is+'.selectAll();">Select All</span> &nbsp; <span class="ctrE" title=" Clear All " onclick="'+this.is+'.clearAll();">Clear All</span> &nbsp; &nbsp;<span id="undo" style="background-color:#ffff99;"></span></td><td width="70%" align="right">'+sCB+'</td></tr></table></td></tr>'}s+='</table>';this.gTxt(this.contain,s);this.ltb();if(this.showFootbar){for(var i=0;i<this.controlButtons.length;i++){var btnid=this.is+'_'+this.controlButtons[i][0];this.sa(btnid,'click', this.controlButtons[i][2]);this.sa(btnid,'mouseover',this.is+'.setStyle("'+btnid+'","mouseOn")');this.sa(btnid,'mouseout',this.is+'.setStyle("'+btnid+'","mouseOut")');}}this.UI=this.rel(this.ID).contentWindow;this.ed=this.UI.document;with(this.ed){designMode='On';open();write('<html><head><style>p{margin:0px;padding:0px;}</style></head><body style="color:'+this.textColor+';font-family:'+this.textFont+';font-size:'+this.textSize+';margin:2px;padding:4px;background-color:'+this.backgroundColor+';'+((this.backgroundImage.url!='')?'background:'+this.backgroundColor+' url(\''+this.backgroundImage.url+'\') '+this.backgroundImage.repeat+' '+this.backgroundImage.position+' fixed;':'')+'"><br>'+this.cnt+'</body></html>');close();}this.UI.focus();};this.fmt=function(c,v){this.cm();if(this.ed.queryCommandEnabled(c)){if(!v){v=null;}this.ed.execCommand(c,false,v);this.UI.focus();}}
  this.addLink=function(){
	this.cm();
    var aLink=prompt('Enter or paste a link :', '');
      if(aLink){
        this.fmt('CreateLink', aLink);
      }
  }
  this.insertImage=function(url){
	this.cm();
	this.UI.focus();
	  var aLink=(url==null)?prompt('Enter or paste a URL :', ''):url;
	  if(aLink){
        this.fmt('InsertImage',  aLink);
	  }
  } 
  this.bs=[
		['btnBold', 'Bold', 'bold.gif', this.is+'.fmt("bold")', 1],
		['btnItalic', 'Italic', 'italic.gif', this.is+'.fmt("italic")', 1],
		['btnUnderline', 'Underline', 'underline.gif', this.is+'.fmt("underline")', 1],
		['btnFontface', 'Font Face', 'fontface.gif', this.is+'.ffl()', 1],
		['btnFontsize', 'Font Size', 'fontsize.gif', this.is+'.fsl()', 1],
		['btnFontcolor', 'Font Color', 'fontcolor.gif', this.is+'.cl(1)', 1],
		['btnBackcolor', 'Background Color', 'bgcolor.gif', this.is+'.cl(2)', 1],
		['btnSuperscript', 'Super Script', 'sup.gif', this.is+'.fmt("superscript")', 1],
		['btnSubscript', 'Sub Script', 'sub.gif', this.is+'.fmt("subscript")', 1],
		['btnInsertLink', 'Insert Link', 'link.gif', this.is+'.addLink()', 1],
		['btnInsertImage', 'Insert Image', 'image.gif', this.is+'.insertImage()', 1],
		['btnEmoticon', 'Emoticons', '8.gif', this.is+'.osl()', 1],
		['btnAlignLeft', 'Align Left', 'alignLeft.gif', this.is+'.fmt("justifyleft")', 1],
		['btnAlignCenter', 'Align Center', 'alignCenter.gif', this.is+'.fmt("justifycenter")', 1],
		['btnAlignRight', 'Align Right', 'alignRight.gif', this.is+'.fmt("justifyright")', 1],
		['btnAlignJustify', 'Align Justify', 'alignJustify.gif', this.is+'.fmt("justifyfull")', 1],
		['btnOrderedList', 'Ordered List', 'orderedList.gif', this.is+'.fmt("insertorderedlist")', 1],
		['btnBulletedList', 'Bulleted List', 'bulletedList.gif', this.is+'.fmt("insertunorderedlist")', 1],
		['btnIndentMore', 'Indent', 'indent.gif', this.is+'.fmt("indent")', 1],
		['btnOutdentMore', 'Outdent', 'outdent.gif', this.is+'.fmt("outdent")', 1],
		['btnQuote', 'Quote', 'quote.png', this.is+'.quote()', 1],
		['btnCode', 'Source Code', 'code.jpg', this.is+'.code()', 1],
		['btnUnformat', 'Remove Formatting', 'removeformatting.gif', this.is+'.unformat()', 1]
  ];
  this.fontSize=[1,2,3,4,5,6,7];
  this.fontFace=['Courier','Georgia','Cursive','Fixedsys','Impact','Serif','Sans-Serif','Elephant'];
  this.colors=[
	['#000000','Black'],
	['#A0522D','Sienna'],
	['#556B2F','Dark Olive Green'],
	['#006400','Dark Green'],
	['#483D8B','Dark Slate Blue'],
	['#000080','Navy'],
	['#4B0082','Indigo'],
	['#2F4F4F','Dark Slate Gray'],
	['#8B0000','Dark Red'],
	['#FF8C00','Dark Orange'],
	['#808000','Olive'],
	['#008000','Green'],
	['#008080','Teal'],
	['#0000FF','Blue'],
	['#708090','Slate Gray'],
	['#696969','Dim Gray'],
	['#FF0000','Red'],
	['#F4A460','Sandy Brown'],
	['#9ACD32','Yellow Green'],
	['#2E8B57','Sea Green'],
	['#48D1CC','Medium Turquoise'],
	['#4169E1','RoyalBlue'],
	['#800080','Purple'],
	['#808080','Gray'],
	['#FF00FF','Magenta'],
	['#FFA500','Orange'],
	['#FFFF00','Yellow'],
	['#00FF00','Lime'],
	['#00FFFF','Cyan'],
	['#00BFFF','Deep SkyBlue'],
	['#9932CC','Dark Orchid'],
	['#C0C0C0','Silver'],
	['#FFC0CB','Pink'],
	['#F5DEB3','Wheat'],
	['#FFFACD','Lemon Chiffon'],
	['#98FB98','Pale Green'],
	['#AFEEEE','Pale Turquoise'],
	['#ADD8E6','Light Blue'],
	['#DDA0DD','Plum'],
	['#FFFFFF','White'],
	['#DDA0DD','Plum'],
	['#FFFFFF','White']
  ];
  this.emoticons=[
	['happy',':)','1.gif'],
	['sad',':(','2.gif'],
	['rose','@};-','53.gif'],
	['coffee','~O)','57.gif'],	
	['batting eyelashes',';;)','5.gif'],
	['big hug','>:D<','6.gif'],
	['confused',':-/','7.gif'],
	['love struck',':x','8.gif'],
	['blushing',':">','9.gif'],
	['tongue',':P','10.gif'],
	['surprise',':-O','13.gif'], 
	['cool','B-)','16.gif'],
	['crying',':((','20.gif'],
	['laughing',':))','21.gif'],
	['straight face',':|','22.gif'],
	['don\'t tell anyone',':-$','32.gif'],
	['yawn','(:|','37.gif'],
	['thinking',':-?','39.gif'],
	['d\'oh','#-o','40.gif'],
	['applause','=D>','41.gif'],
	['angel','O:)','25.gif'],
	['daydreaming','8->','105.gif'],
	['kiss',':-*','11.gif'],
	['angry','X(','14.gif'],
	['worried',':-S','17.gif'],
	['waiting',':-w','45.gif'],
	['I don\'t know',':-??','106.gif'],
	['shame on you','[-X','68.gif'],
	['whistling',':-"','65.gif'],
	['praying','[-O<','63.gif'],
	['winking','	;)','3.gif'],
	['big grin',':D','4.gif']
  ];
  this.controlButtons=[
	['btnSubmit', 'Gửi', 'doPost()']
	//['btnReset', 'Reset', 'doReset()'],
	//['btnCancel', 'Cancel', 'doCancel()']
  ];
}
//function doReset(){alert('Bạn đã gọi hàm "doReset" chưa được định nghĩa.\n\n Hãy tìm đến dòng 154... ');}
//function doCancel(){alert('Bạn đã gọi hàm "doCancel" chưa được định nghĩa.\n\n Hãy tìm đến dòng 155... ');}
function doPost()
{
      //var content = RTE.getText();
  var content = RTE.getHTML();
  document.myform.content.value = content;
  if(content.length==0){
      alert("Bạn chưa nhập nội dung");
  } else{
      document.forms["myform"].submit();
  }
}