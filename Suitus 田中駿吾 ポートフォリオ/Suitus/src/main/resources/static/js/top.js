
$(function(){
  $(".btn").on("click", function(){
	$(".btn").toggleClass("active");
    $(".sp-menu").toggleClass("open");
  });
  

/*  sp-menuの現urlに一致するaタグにactiveクラス(薄い背景)を付与　*/  
　　
　　let pathName = $(location).attr('pathname');

	$.each($(".sp-menu a"), function(i, a){
/*	aの中身<a href="/ball" class="active">ボール</a>　*/
			if(a.getAttribute("href")==pathName) {
					/* aをオブジェクト化 */
					$(a).addClass("active");
					
				  };
	      })
		  let path = $(location).attr('pathname');
		  let lastSlashIndex = path.lastIndexOf('/');

		  // 最後の'/'以降の文字列を切り出す
		  // lastIndexOf('/') + 1 とすることで、'/'自体を含めずに次の文字から取得できる
		  let lastSegment = path.substring(lastSlashIndex + 1);
		  console.log(lastSegment);
		  
		  
		  //背景画像にしたい画像パスを列挙
		  const images = {ball: 'useball.jpg',
			shoes: 'useshoes.jpg',
			training: 'useTrainingGoods.jpg',
			cap: 'cap.jpg'
		  };
		  $("#useImage").attr('src','/image/' + images[lastSegment]);
	  })