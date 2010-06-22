(function() {

    function xPath(node, path)
    {
        if (!node) return null;
        if (!path) path = [];

        if (node.parentNode) {
            path = xPath(node.parentNode, path);
        }
        var count = 0, sibling = null;

        if (node.previousSibling) {

            count = 1;
            sibling = node.previousSibling;

            do {
                if (sibling.nodeType == 1 && sibling.nodeName == node.nodeName) count++;
                sibling = sibling.previousSibling;
            } while (sibling);

            if (count == 1) count = 0;

        } else if (node.nextSibling) {

            sibling = node.nextSibling;

            do {
                if (sibling.nodeType == 1 && sibling.nodeName == node.nodeName) {
                    count = 1;
                    sibling = null;
                } else {
                    count = 0;
                    sibling = sibling.previousSibling;
                }
            } while (sibling);

        }

        if (node.nodeType == 1) {
            var suffix = '';

            if ('' != node.id)  suffix = "[@id='"+node.id+"']";
            else if (count > 1) suffix = '['+count+']';

            path.push(node.nodeName + suffix);
        }

        return path;
    }


    document.addEventListener('contextmenu', function(evt) {
        if (!evt) var evt = window.event;

        document.title = 'xpath: /' + xPath(evt.target).join('/');

        evt.cancelBubble = true;
        if (evt.stopPropagation) evt.stopPropagation();
    }, false);

    document.addEventListener('mouseover', function(evt) {
        if (!evt) var evt = window.event;

        window.status = 'xpath: /' + xPath(evt.target).join('/');
        evt.target.style.outline = '#f00 solid 2px';

        evt.cancelBubble = true;
        if (evt.stopPropagation) evt.stopPropagation();
    }, false);

    document.addEventListener('mouseout', function(evt) {
        if (!evt) var evt = window.event;

        evt.target.style.outline = 'none';

        evt.cancelBubble = true;
        if (evt.stopPropagation) evt.stopPropagation();
    }, false);

    window.status = 'xpath: /' + xPath(document.body).join('/');

})();
