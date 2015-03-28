//First lets initiate some variables

var browser = navigator.userAgent.toLowerCase();
var isEditable = false;
var isIE = ((browser.indexOf("msie") != -1) && (browser.indexOf("opera") == -1) && (browser.indexOf("webtv") == -1));
var isGecko = (browser.indexOf("gecko") != -1);
var isSafari = (browser.indexOf("safari") != -1);
var isKonqueror = (browser.indexOf("konqueror") != -1);
var contentFrameId;
var contentDocument;
var contentWindow;
var viewMode = 1; // 1 = WYSIWYG mode (text), 2 = HTML mode (html source)

// params: name/id of hidden datafield, width, height
function insertEditor(editor, width, height) {
  // isEditable=true if the browser is not safari or konqueror
  if (document.getElementById && document.designMode && !isSafari && !isKonqueror) {
    isEditable= true;
  }

  //Javascript function displayEditor will create the text area for user input.
  if(isEditable) {
    contentFrameId = editor + "Frame";
    document.writeln('<iframe id="' + contentFrameId + '" name="' + editor + 'Frame" width="' + width + 'px" height="' + height + 'px" style="background: #FFF; border: solid 1px black;"></iframe>');
    //get html (textarea value) from hiddeneditor
    var html = document.getElementById(editor).value;

    contentWindow = document.getElementById(contentFrameId).contentWindow;
    contentDocument = contentWindow.document;
    var mainContent= "<html id=" + contentFrameId + "><head></head><body>" + html + "</body></html>" ;
    contentDocument.write(mainContent);

    if(!isIE) {
      contentDocument = document.getElementById(contentFrameId).contentDocument;
    }

    //enable designMode
    contentDocument.designMode = "on" ;
    contentDocument.contentEditable = "true" ;
    
    // set listeners
    contentDocument.addEventListener('keyup', new Function("e", "handleOnKeyUp('" + contentDocument + "', e);"), false);
    contentDocument.addEventListener('mouseup', new Function("updateButtons('" + contentDocument + "', true);"), false);

    updateButtons(contentDocument, true);    
  } else {
    document.writeln('<textarea name="' + editor + '" id="' + editor + '" cols="39" rows="10">' + html + '</textarea>');
  }
}

//To execute command we will use javascript function execCommand.
function editorCommand(command, option) {
  // first we assign the content of the textarea to the variable mainField
  var mainField = document.getElementById(contentFrameId).contentWindow;
  // then we will use execCommand to execute the option on the textarea making sure the textarea stays in focus
  try {
    mainField.focus();
    mainField.document.execCommand(command, false, option);
    mainField.focus();
  } catch (e) {
  }
  updateButtons(contentDocument, true);
}

function toggleView() {
  // change the font
  if(viewMode == 2){
    contentWindow.document.body.style.fontFamily = '';
    contentWindow.document.body.style.fontSize = '';
    contentWindow.document.body.style.color = '';
    contentWindow.document.body.style.fontWeight = '';
    contentWindow.document.body.style.backgroundColor = '';
    document.getElementById('toolbar').style.visibility='visible';
  }else{
    contentWindow.document.body.style.fontFamily = 'monospace';
    contentWindow.document.body.style.fontSize = '10pt';
    contentWindow.document.body.style.color = '#000';
    contentWindow.document.body.style.backgroundColor = '#fff';
    contentWindow.document.body.style.fontWeight = 'normal';
    document.getElementById('toolbar').style.visibility='hidden';
  }
    
  if (isIE)
  {
    if(viewMode == 2){
        contentWindow.document.body.innerHTML = contentWindow.document.body.innerText;
        contentWindow.focus();
        this.viewMode = 1; // WYSIWYG
        updateButton("btnHTML", false);
    }else{
        contentWindow.document.body.innerText = contentWindow.document.body.innerHTML;
        contentWindow.focus();
        this.viewMode = 2; // Code
        updateButton("btnHTML", true);
    }
  }
  else
  {
    if(viewMode == 2){
        var html = contentWindow.document.body.ownerDocument.createRange();
        html.selectNodeContents(contentWindow.document.body);
        contentWindow.document.body.innerHTML = html.toString();
        contentWindow.focus();
        this.viewMode = 1; // WYSIWYG
        updateButton("btnHTML", false);
    }else{
        var html = document.createTextNode(contentWindow.document.body.innerHTML);
        contentWindow.document.body.innerHTML = '';
        contentWindow.document.body.appendChild(html);
        contentWindow.focus();
        this.viewMode = 2; // Code
        updateButton("btnHTML", true);
    }
  }
}

function updateEditor(editor) {
  if (!isEditable)
    return;

  //assign the value of the textarea to the hidden field.
  var hiddenField = document.getElementById(editor);
  if (hiddenField.value == null)
    hiddenField.value = "";
  hiddenField.value = document.getElementById(contentFrameId).contentWindow.document.body.innerHTML;
}

function handleOnKeyUp(doc, e) {
  if (e.ctrlKey || (e.keyCode >= 33 && e.keyCode <= 40)) {
    updateButtons(doc, false);
  }  
}

function insertNodeAtSelection(node) {
  var win = document.getElementById(contentFrameId).contentWindow;
  var sel;
  if (win.getSelection) {
    sel = win.getSelection();
  } else if (win.document.getSelection) {
    sel = win.document.getSelection();
  } else if (win.document.selection) {
    sel = win.document.selection.createRange().text;
  }
  var range = sel.getRangeAt(0);
  sel.removeAllRanges();
  range.deleteContents();
  var container = range.startContainer;
  var pos = range.startOffset;
  range = contentDocument.createRange();
  
  // special handling of text-node (nodeType == 3)
  if (container.nodeType == 3 && node.nodeType == 3) {
    container.insertData(pos, node.nodeValue);
    // put cursor after inserted text
    range.setEnd(container, pos+node.length);
    range.setStart(container, pos+node.length);
  } else {
    var afterNode;
    if (container.nodeType == 3) {
      //split textnode into 2 nodes and place node between them
      var textNode = container;
      container = textNode.parentNode;
      var text = textNode.nodeValue;
      var textBefore = text.substr(0, pos);
      var textAfter = text.substr(pos);
      var beforeNode = contentDocument.createTextNode(textBefore);
      var afterNode = contentDocument.createTextNode(textAfter);
      
      container.insertBefore(afterNode, textNode);
      container.insertBefore(node, afterNode);
      container.insertBefore(beforeNode, node);
      
      container.removeChild(textNode);
    } else {
      afterNode = container.childNodes[pos];
      container.insertBefore(node, afterNode);
    }
    range.setEnd(afterNode, 0);
    range.setStart(afterNode, 0);
  }
  sel.addRange(range);
  sel.removeAllRanges();
}

function formatParagraph(formatSelector) {
  var paragraphFormat = formatSelector.options[formatSelector.selectedIndex].value;
  contentDocument.execCommand('formatBlock', false, paragraphFormat);
  formatSelector.selectedIndex = 0;

  var win = document.getElementById(contentFrameId).contentWindow;
  win.focus();
  updateButtons(contentDocument, true);
}

function imageDialog_callback(dialog) {
  var rv = dialog.returnValue;
  if (rv != null) {
    var result;
    
    var img = contentDocument.createElement("img");
    img.setAttribute("align", rv.align);    
    img.setAttribute("alt", rv.description);
    img.setAttribute("border", rv.border);
    img.setAttribute("hspace", rv.hspace);
    img.setAttribute("name", rv.name);
    if (rv.previewUrl != null && rv.previewUrl != "")
    {
      img.setAttribute("src", rv.previewUrl);
    }
    else
    {
      img.setAttribute("height", rv.height);
      img.setAttribute("src", rv.originalUrl);
      img.setAttribute("width", rv.width);
    }
    img.setAttribute("title", rv.description);
    img.setAttribute("vspace", rv.vspace);
    
    if (rv.title == null || rv.title == "") {
      result = img;
    } else {
      // create an image with title under image
      var div = contentDocument.createElement("div");
      var width = 200;
      if (rv.width != null && rv.width != "") {
        width = rv.width;
      }
      div.setAttribute("style", "float: " + rv.align + "; text-align: center; padding: 5px; min-width: " + width + "px;");

      img.removeAttribute("align");      
      div.appendChild(img);
      
      var br = contentDocument.createElement("br");
      div.appendChild(br);
      
      var titleTxt = contentDocument.createTextNode(rv.title);
      div.appendChild(titleTxt);
      
      var border = 0;
      if (rv.border != null && rv.border != "") {
        border = rv.border;
      }
/*      div.setAttribute("style", div.getAttribute("style") + " border: solid #AAA " + rv.border + "px;"); */
      div.setAttribute("style", div.getAttribute("style") + " border: solid #AAA 1px;");
      
      var hspace = 5;
      var vspace = 2;
      if (rv.hspace != null && rv.hspace != "") {
        hspace = rv.hspace;
      }
      if (rv.vspace != null && rv.vspace != "") {
        vspace = rv.vspace;
      }
      div.setAttribute("style", div.getAttribute("style") + " margin: " + vspace + "px " + hspace + "px;");
      result = div;
    }
    insertNodeAtSelection(result);
  }
    
  updateButton("btnImage", false);
  contentWindow.focus();
}
  
function openTableDialog() {
  var dialog = window.open(dialogTableUrl, "Table", "status=no,modal=yes,width=350,height=150");
}

function tableDialog_callback(dialog) {
  var rv = dialog.returnValue;
  if (rv != null) {
    var table = contentDocument.createElement("table");
    table.border = "1";
    
    // headers
    if (rv.headers) {
      if (rv.headers == true) {
        var tr = contentDocument.createElement("tr");
        for (var j=1; j<=parseInt(rv.cols); j++) {
          var th = contentDocument.createElement("th");
          th.innerHTML = "" + j;
          tr.appendChild(th);
        }
        table.appendChild(tr);
      }
    }
    
    // data cells
    for (var i=0; i<parseInt(rv.rows); i++) {
      var tr = contentDocument.createElement("tr");
      for (var j=0; j<parseInt(rv.cols); j++) {
        var td = contentDocument.createElement("td");
        td.innerHTML = "&nbsp;";
        tr.appendChild(td);
      }
      table.appendChild(tr);
    }
    insertNodeAtSelection(table);
  }
    
  updateButton("btnTable", false);
  contentWindow.focus();
}
  
function submitForm() {
  updateEditor(dataFieldId);
  return true;
}

// button bar functions
function getBaseImageName(image) {
  var imgSrc = image.src;
  return baseImageName = imgSrc.substring(0, imgSrc.lastIndexOf("_"));
}

function drawButtonDown(image) {
  image.src = getBaseImageName(image) + "_down.gif";
}

function drawButtonUp(image) {
  image.src = getBaseImageName(image) + "_up.gif";
}

function updateButtons(doc, force) {
  contentWindow.focus();
  updatableButtons = [
     // command, buttonId
    ["bold", "btnBold"],
//    ["indent", "btnIndent"],
    ["InsertOrderedList", "btnOrderedList"],
    ["InsertUnorderedList", "btnUnorderedList"],
    ["italic", "btnItalic"],
    ["justifycenter", "btnJustifyCenter"],
    ["justifyleft", "btnJustifyLeft"],
    ["justifyright", "btnJustifyRight"],
//    ["outdent", "btnOutdent"],
    ["Subscript", "btnSubscript"],
    ["Superscript", "btnSuperscript"],
    ["underline", "btnUnderline"]
  ];
  
  for (var i=0; i<updatableButtons.length; i++) {
    try {
      updateButton(updatableButtons[i][1], contentDocument.queryCommandState(updatableButtons[i][0]));
    } catch (ex) {}
  }
  
  // update block format dropdown
  var blockFormat = contentDocument.queryCommandValue("formatBlock");
  var selBlockFormat = document.getElementById("selBlockFormat");
  selBlockFormat.options[0].selected = true;
  for (var i=0; i<selBlockFormat.options.length; i++) {
  	if (selBlockFormat.options[i].value == blockFormat ||
  	    selBlockFormat.options[i].value == "<" + blockFormat + ">") {
      selBlockFormat.options[i].selected = true;
  	} else {
  	  selBlockFormat.options[i].selected = false;
  	}
  }
}

function updateButton(btnId, enable) {
  //alert(btnId + " / " + enable);
  var button = document.getElementById(btnId);
  if (enable) {
    button.src = getBaseImageName(button) + "_down.gif";
  }
  else
  {
    button.src = getBaseImageName(button) + "_up.gif";
  }
}
