LightBox2Panel:
---------------
- Based on "Lightbox2 integration with Wicket"
(see http://www.codesmell.org/org.codesmell.wicket.lightbox/index.html)
- Updated to Wicket 1.4.7
- Added automatic resizing to fit screen size
(see http://blog.hma-info.de/2008/04/09/latest-lightbox-v2-with-automatic-resizing/)
- Added fix by Steve Schuckman:
219: $('overlay').setStyle({height: arrayPageSize[1] + 'px' }); //Change Steve Schuckman from arrayPageSize[3] to arrayPageSize[1]
306: $('overlay').setStyle({height: arrayPageSize[1] + 'px' }); //Change Steve Schuckman from arrayPageSize[3] to arrayPageSize[1]
(see http://blog.hma-info.de/2008/04/09/latest-lightbox-v2-with-automatic-resizing/#comment-11037)

- Added webapp-context awareness for lightbox image paths
(see lightbox.js: fileLoadingImage, fileBottomNavCloseImage)
- Added workaround to TextTemplate replacer bug:
replaced '$$' with '$$$' in lightbox.js

TODO:
-----
- ensure proper order of loading JS-files in LightBox2Panel.java, maybe like this:

It can be accomplished by the component hierarchy, by knowing that parent
components' references are added first or in other words renderHead is
invoked earlier.

So you may have a:

class AbstractPrototypScriptaculousBehaviour extends AbstractBehaviour {
  renderHead( response ){
    super.renderHead( response  );
    response.renderJavascrtReference( new JavascriptResourceReference(
this.getClass(), "js/prototype.js" ), "prototype" );
    response.renderJavascrtReference( new JavascriptResourceReference(
this.getClass(), "js/scriptaculous.js?load=effects,builder" ),
"scriptaculous" );
  }
}

class LightBoxBehaviour extends AbstractPrototypScriptaculousBehaviour {
  renderHead( response ){
    super.renderHead( response  );
    response.renderJavascrtReference( new JavascriptResourceReference(
this.getClass(), "lightbox.js" ), "lightbox" );
  }
}
---------------------------------------------------
- Adding "original size" link like this:
//At top of page add 
var IsResized = false;
....
//Then in updateDetails add
if(IsResized) {
	this.numberDisplay.update( LightboxOptions.labelImage + ' ' + (this.activeImage + 1) + ' ' + LightboxOptions.labelOf + ' ' + this.imageArray.length + ' <a href="' + this.imageArray[this.activeImage][0] + '" rel="nofollow">Original Size</a>').show();
} else {
	this.numberDisplay.update( LightboxOptions.labelImage + ' ' + (this.activeImage + 1) + ' ' + LightboxOptions.labelOf + '  ' + this.imageArray.length).show();
}
 
//And in adjustImageSize change the maxwidth check like this
if (imgWidth < maxWidth || imgHeight > maxHeight) {
	IsResized = true;
	imgWidth = maxWidth;
	imgHeight = maxHeight;
} else {
	IsResized = false;
}

(see http://blog.hma-info.de/2008/04/09/latest-lightbox-v2-with-automatic-resizing/#comment-24027)
(see