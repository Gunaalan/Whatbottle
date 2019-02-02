package com.whatbottle.util;

import com.lithium.mineraloil.api.lia.LIAAPIConnection;
import com.lithium.mineraloil.api.lia.api.models.Board;
import com.lithium.mineraloil.api.lia.api.models.Message;
import com.lithium.mineraloil.api.lia.api.models.User;
import com.lithium.mineraloil.api.lia.api.v1.BoardV1API;
import com.lithium.mineraloil.api.lia.api.v1.CategoryV1API;
import com.lithium.mineraloil.api.lia.api.v1.models.BoardV1Response;
import com.lithium.mineraloil.api.lia.api.v1.models.CategoryV1Response;
import com.lithium.mineraloil.api.lia.api.v2.BoardV2API;
import com.lithium.mineraloil.api.lia.api.v2.models.BoardV2;
import com.lithium.mineraloil.api.lia.api.v2.models.Category;
import com.lithium.mineraloil.api.rest.APIVersion;
import com.lithium.mineraloil.api.rest.RestAPIException;
import com.lithium.mineraloil.api.rest.models.APIUser;
import com.whatbottle.data.ConversationStyles;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;

@Slf4j
public class PostMesageToLIA {
    static LIAAPIConnection liaapiConnection;

    public static void main(String args[]) {
        PostMesageToLIA postMesageToLIA = new PostMesageToLIA();
        User user = postMesageToLIA.getDefaultUser();
        String boardId = "boardId2skkk4";
        String boardTitle = "boardTitkkle";

        liaapiConnection = postMesageToLIA.getLIAAPIConnectionV1(user, "automationresp1.qa.lithium.com", -1, "automationresp1");
        Board board = postMesageToLIA.createLIABoard(liaapiConnection, boardId, boardTitle, "hello1", ConversationStyles.forum);

        Message message = Message.builder()
                .subject("this is subject")
                .body("This is body")
                .build();
        postMesageToLIA.postMessage(liaapiConnection, board, user, message);
    }

    public  Board createLIABoard(LIAAPIConnection liaapiConnection, String boardId, String boardTitle, String liaCategory, ConversationStyles conversationStyles) {
        Category category = Category.builder().id(liaCategory).build();
        createCategory(liaapiConnection, liaCategory, liaCategory);
        BoardV2 board = BoardV2.builder()
                .type("board")
                .parentCategory(category)
                .id(boardId)
                .conversation_style(conversationStyles.toString())
                .title(boardTitle)
                .build();

        return createBoard(new BoardV2API(liaapiConnection).createBoard(board));
    }

    public  CategoryV1Response createCategory(LIAAPIConnection connection, String categoryId, String categoryTitle) {
        CategoryV1Response categoryV1Response = null;
        try {
            categoryV1Response = new CategoryV1API(connection).createCategory(categoryId, categoryTitle);
        } catch (RestAPIException restAPIException) {
            log.info("Category with already available with ID " + categoryId);
        }
        return categoryV1Response;
    }

    public static Board createBoard(BoardV2 boardV2) {
        Board board = Board.builder()
                .type(boardV2.getType())
                .viewHref(boardV2.getViewHref())
                .href(boardV2.getHref())
                .boardName(boardV2.getTitle())
                .boardType("boards")
                .boardId(boardV2.getId())
                .id(boardV2.getId())
                .build();
        return board;
    }

    public BoardV1Response postMessage(LIAAPIConnection liaapiConnection, Board board, User user, Message message) {

        BoardV1Response messagePostResponse = new BoardV1API(liaapiConnection)
                .postMessage(board, message);
        return messagePostResponse;


    }

    public User getDefaultUser() {
        User defaultAdmin = User.builder()
                .username("admin")
                .email("admin@lithium.com")
                .password("arfarf")
                .firstname("admin")
                .build();

        return defaultAdmin;
    }

    public LIAAPIConnection getLIAAPIConnectionV1(User user, String url, int port, String community) {
        APIUser apiUser = new APIUser();
        apiUser.setEmail(user.getEmail());
        apiUser.setPassword(user.getPassword());
        apiUser.setUsername(user.getUsername());
        apiUser.setName(user.getFirstname());
        return LIAAPIConnection.builder()
                .user(apiUser)
                .httpHost(new HttpHost(url, port, "http"))
                .version(APIVersion.VC)
                .community(community)
                .build();
    }


}
