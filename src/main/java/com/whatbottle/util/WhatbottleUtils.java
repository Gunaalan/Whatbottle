package com.whatbottle.util;

import org.springframework.stereotype.Component;

/**
 * Created by guna on 30/06/18.
 */
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.security.Keys;
import io.smooch.client.ApiClient;
import io.smooch.client.ApiException;
import io.smooch.client.Configuration;
import io.smooch.client.api.AppUserApi;
import io.smooch.client.api.ConversationApi;
import io.smooch.client.api.WebhookApi;
import io.smooch.client.auth.*;
import io.smooch.client.api.AppApi;
import io.smooch.client.model.AppCreate;
import io.smooch.client.model.AppResponse;
import io.smooch.client.model.ListWebhooksResponse;
import io.smooch.client.model.MessagePost;
import io.smooch.client.model.MessageResponse;
import io.smooch.client.model.WebhookResponse;

import java.io.File;
import java.util.*;

import static io.jsonwebtoken.JwsHeader.KEY_ID;

public class WhatbottleUtils {


}