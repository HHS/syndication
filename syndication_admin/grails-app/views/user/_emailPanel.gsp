<div class="col-md-4">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h1 class="panel-title">${name}</h1>
        </div>
        <div class="panel-body">
            <div class="scroll">
                <ul>
                    <g:each in="${list}" var="dom">
                        <li>${dom}</li>
                    </g:each>
                </ul>
            </div>
        </div>
    </div>
</div>