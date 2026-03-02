/**
 * 
 */

  

const inputAddress = document.getElementById("inputAddress");
const purchaseForm = document.getElementById("purchaseForm");
 
  // 1,000円を1000の整数として抽出
      const price = parseInt(document.getElementById('priceDisplay').textContent.replace("円", "").replace(",",""));
	  const totalDisplay = document.getElementById('totalDisplay');
	  
	  //合計金額と個数の値を初期化
	  totalDisplay.textContent = price * 1;
	  quantityInput.value = 1;
	  
	  //個数入力イベント時、合計金額を表示
      quantityInput.addEventListener('input', (e) => {
			if (quantityInput.value > 10) {
		      quantityInput.value = 10;
		    }
		    if (quantityInput.value < 1) {
		      quantityInput.value = 1;
		    }
		//e.targetはイベントが発生した要素
          const qty = parseInt(e.target.value);
          const total = price * qty;
          totalDisplay.textContent = total;
      });
	  
	  const totalPrice = document.getElementById("totalPrice");
	      const buyButton = document.getElementById("buyButton");
	      const modal = document.getElementById("confirmModal");

	      const confirmQuantity = document.getElementById("confirmQuantity");
	      const confirmTotal = document.getElementById("confirmTotal");
	      const confirmAddress = document.getElementById("confirmAddress");

	      const cancelButton = document.getElementById("cancelButton");
	      const decideButton = document.getElementById("decideButton");
	      const addressNoneWarn = document.getElementById("addressNoneWarn");
	
		  //購入ボタン押下時、確認画面に合計金額、住所、個数を表示
	      buyButton.addEventListener("click", () => {
	        const qty = Number(quantityInput.value);
	        const address = document.getElementById("inputAddress").value;
			decideButton.style.display = address.trim() ? "block" : "none";
			console.log(addressNoneWarn);
			addressNoneWarn.textContent = address.trim() ? "" : "お届け先の住所を入力してください。";
	        confirmQuantity.textContent = qty;
	        confirmTotal.textContent = (price * qty).toLocaleString();
	        confirmAddress.textContent = address;

	        modal.style.display = "flex";
	      });

		  //キャンセルボタン押下時はモーダル非表示
	      cancelButton.addEventListener("click", () => {
	        modal.style.display = "none";
	      });

		  //購入確定ボタン押下時、alertでポップアップメッセージを表示
	      decideButton.addEventListener("click", () => {
	        alert("購入が完了しました！2日以内にそちらに郵送いたします。");
	        modal.style.display = "none";
	      });