package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.db.model.userdata.FriendsEntity;

import java.util.Objects;
import java.util.UUID;

public class FriendsJson {
    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("friends_id")
    private UUID friendsId;

    @JsonProperty("pending")
    private boolean pending;

    public FriendsJson() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(UUID friendsId) {
        this.friendsId = friendsId;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendsJson that = (FriendsJson) o;

        if (pending != that.pending) return false;
        if (!Objects.equals(userId, that.userId)) return false;
        return Objects.equals(friendsId, that.friendsId);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (friendsId != null ? friendsId.hashCode() : 0);
        result = 31 * result + (pending ? 1 : 0);
        return result;
    }

    public static FriendsJson fromEntity(FriendsEntity entity) {
        FriendsJson friendship = new FriendsJson();
        friendship.setUserId(entity.getUser().getId());
        friendship.setFriendsId(entity.getFriend().getId());
        return friendship;
    }
}
