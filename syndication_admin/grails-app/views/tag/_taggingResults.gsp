<div class="success">
    <ul>
    <g:each in="${successfullyTaggedItems}" var="mediaItem">
        <li><span class="text-success"><i class="fa fa-check-circle"></i></span> ${mediaItem.name}</li>
    </g:each>
    %{--<g:each in=""--}%
    </ul>
</div>