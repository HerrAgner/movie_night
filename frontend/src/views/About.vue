<template>
    <v-container>
        <h1>JWT</h1>
        <p>I'm at a restaurant and I order a beer. This particular restaurant also takes age verification very
            seriously. The waiter asks to see my license, and I provide it. The waiter pulls out a UV light and inspects
            the watermark on my license. It checks out ok, so he hands the license back and gets my beer.
            
            In this case, the issuing authority of the license placed a special "seal" into the license that can be used
            to identify valid licenses. This means that verification can be performed without calling back to the DMV.
            The waiter has to know exactly what seal to look for. That might mean he has to go look up the state's seal
            sometimes. Once the waiter determines that the license is valid, they can trust the Date of Birth
            information on it.
            
            This is the JWT variety of authentication. Once the DMV believes you are who you say (JWT version:
            autheticated, probably with password), it collects various data about you (JWT version: claims) and puts it
            on the license (JWT itself). When the license is issued, it is also watermarked with a seal (JWT version:
            digital signature) so that it can be examined for validity by people who know what to look for (JWT version:
            your API validates the JWT signature with a shared key). After that, the license (JWT) is trusted and the
            Date of Birth (claim) is assumed to be true.
        </p>
        <h1>
            https
        </h1>
        <p>Http tells a website "give me the page."
            
            Https says "give me this page, but keep our interaction secret, and prove you are who I think you are."
            
            Imagine http is you shouting across a crowded bus station in a common language at another person, and them
            shouting back in the same language.
            
            Now imagine https is both of you in that crowded bus station, but you're speaking in your own language only
            you two know, after you found out, and proved they were who they said you were.
            The "s" means secure.
        </p>
        <h1>Oauth2</h1>
        <p>Remember when you had to say a special password to enter your friend's tree house? It's the same way on the
            internet: you have to type in a password before you can use stuff like Facebook and Twitter. So that's why
            you keep this password secret, because anyone else who knows it can steal all your information and worse,
            pretend that they're you.
            
            OAuth is a way of telling your Facebook and Twitter that they can share parts of your information (not
            everything) with other websites, without revealing your super duper secret password to these other websites.
            In other words it's a way of providing permission. And in case you don't like how other websites are using
            the information you share, you can always take away this permission at any time.
            Before you provide permission to someone else, you have to enter your password to let Facebook and Twitter
            know that you're really the one providing the permission. Your password isn't shared with anyone else
            through OAuth.
        </p>
        <h1>OpenID Connect</h1>
        <p>OpenID connect will give you an access token plus an id token. The id token is a JWT and contains information
            about the authenticated user. It is signed by the identity provider and can be read and verified without
            accessing the identity provider.
            
            In addition, OpenID connect standardizes quite a couple things that oauth2 leaves up to choice. for instance
            scopes, endpoint discovery, and dynamic registration of clients.
            
            This makes it easier to write code that lets the user choose between multiple identity providers.</p>
    </v-container>
</template>

