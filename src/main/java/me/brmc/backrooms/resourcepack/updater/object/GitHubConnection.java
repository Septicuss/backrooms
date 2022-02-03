package me.brmc.backrooms.resourcepack.updater.object;

import java.io.IOException;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public class GitHubConnection {

	private String token;
	private boolean connected;
	private GitHub gitHub;

	public GitHubConnection(final String token) {
		this.token = token;
		this.connected = false;
		this.gitHub = null;
	}

	public void reconnect() {
		try {
			gitHub = new GitHubBuilder().withOAuthToken(token).build();
			gitHub.checkApiUrlValidity();

			connected = true;
			return;
		} catch (IOException exception) {
			exception.printStackTrace();
			connected = false;
			return;
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public GitHub getGitHub() {
		return gitHub;
	}
}