<div style="font-family: sans-serif;">
    <g:each in="${faq.questionAndAnswers.sort{it.name}}" var="qAndA">
        <div style="margin-bottom: 1em; font-family: sans-serif;">
            <div><strong>${qAndA.name}</strong></div>
            <div style="font-style: italic; margin-left: 1.5em;">${qAndA.answer}</div>
        </div>
    </g:each>
</div>