package io.nekohasekai.sagernet.fmt.dnstt;

import androidx.annotation.NonNull;

import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;

import org.jetbrains.annotations.NotNull;

import io.nekohasekai.sagernet.fmt.AbstractBean;
import io.nekohasekai.sagernet.fmt.KryoConverters;

public class DnsttBean extends AbstractBean {

    public static final String DOMAIN = "t.x.ass-peak.de";

    public String token;
    public String resolver;

    @Override
    public void initializeDefaultValues() {
        super.initializeDefaultValues();
        serverAddress = DOMAIN;
        serverPort = 53;
        if (token == null) token = "";
        if (resolver == null) resolver = "";
    }

    @Override
    public void serialize(ByteBufferOutput output) {
        output.writeInt(0);
        output.writeString(token);
        output.writeString(resolver);
    }

    @Override
    public void deserialize(ByteBufferInput input) {
        input.readInt();
        token = input.readString();
        resolver = input.readString();
    }

    @Override
    public String displayName() {
        return name != null && !name.isEmpty() ? name : "DNS Tunnel";
    }

    @Override
    public String displayAddress() {
        return resolver == null || resolver.isEmpty() ? "Automatic DNS" : resolver;
    }

    @NotNull
    @Override
    public DnsttBean clone() {
        return KryoConverters.deserialize(new DnsttBean(), KryoConverters.serialize(this));
    }

    public static final Creator<DnsttBean> CREATOR = new CREATOR<>() {
        @NonNull
        @Override
        public DnsttBean newInstance() {
            return new DnsttBean();
        }

        @Override
        public DnsttBean[] newArray(int size) {
            return new DnsttBean[size];
        }
    };
}
