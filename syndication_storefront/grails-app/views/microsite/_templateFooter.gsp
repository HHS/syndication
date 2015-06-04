        <div class="microsite-footer row">

            <div class="footer-left col-xs-6">
                <p>${microSite?.footerText}</p>
            </div>

            <div class="col-xs-1"></div>

            <div class="footer-right col-xs-5">
                <ul>
                    <g:if test="${microSite?.footerLinkName1 || microSite?.footerLink1}">
                        <li>
                            <g:link class="footer-link" url="${microSite.footerLink1 ?: "#"}">${microSite.footerLinkName1 ?: microSite?.footerLink1}</g:link>
                        </li>
                    </g:if>
                     <g:if test="${microSite?.footerLinkName2 || microSite?.footerLink2}">
                         <li>
                             <g:link class="footer-link" url="${microSite.footerLink2 ?: "#"}">${microSite.footerLinkName2 ?: microSite?.footerLink2}</g:link>
                         </li>
                     </g:if>
                    <g:if test="${microSite?.footerLinkName3 || microSite?.footerLink3}">
                        <li>
                            <g:link class="footer-link" url="${microSite.footerLink3 ?: "#"}">${microSite.footerLinkName3 ?: microSite?.footerLink3}</g:link>
                        </li>
                    </g:if>
                    <g:if test="${microSite?.footerLinkName4 || microSite?.footerLink4}">
                        <li>
                            <g:link class="footer-link" url="${microSite.footerLink4 ?: "#"}">${microSite.footerLinkName4 ?: microSite?.footerLink4}</g:link>
                        </li>
                    </g:if>
                </ul>
            </div>

            <div class="ownership-blirp row">Powered by <a
                    href="https://digitalmedia.hhs.gov">HHS Digital Media Services</a></div>
        </div><!-- end microsite-footer  -->