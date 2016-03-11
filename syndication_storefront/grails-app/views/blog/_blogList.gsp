<g:each in="${blogs}" var="blog">

    <div class="microsite-article on-white">

        ${raw(blog?.json?.results?.content[0])}

    </div><!-- end microsite-article  -->

</g:each>