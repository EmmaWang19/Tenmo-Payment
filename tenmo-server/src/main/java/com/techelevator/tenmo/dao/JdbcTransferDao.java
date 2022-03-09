package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Transfer> getHistory() {
        String sql = "select transfer_id, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc, uf.username as user_from, uto.username as user_to, amount\n" +
                "from transfer \n" +
                "join transfer_status \n" +
                "on transfer_status.transfer_status_id = transfer.transfer_status_id \n" +
                "join transfer_type \n" +
                "on transfer_type.transfer_type_id = transfer.transfer_type_id\n" +
                "join account af on af.account_id = transfer.account_from \n" +
                "join account ato on ato.account_id = transfer.account_to\n" +
                "join tenmo_user uf on uf.user_id = af.user_id \n" +
                "join tenmo_user uto on uto.user_id = ato.user_id";
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(sql);

        List<Transfer> results = new ArrayList<>();
        while(resultSet.next()){
            results.add(mapRowToTransfer(resultSet));
        }
        return results;
    }

    @Override
    public List<Transfer> viewPendingRequests() {
        return null;
    }

    @Override
    public Transfer createTransfer(boolean request) {
        return null;
    }

    @Override
    public Transfer updatePending(Transfer transfer, Long id) {
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet resultSet){
        Transfer transfer = new Transfer();
        transfer.setId(resultSet.getLong("transfer_id"));
        transfer.setTransferType(resultSet.getString("transfer_type_desc"));
        transfer.setTransferStatus(resultSet.getString("transfer_status_desc"));
        transfer.setAccountFrom(resultSet.getString("user_from"));
        transfer.setAccountTo(resultSet.getString("user_to"));
        transfer.setAmount(resultSet.getBigDecimal("amount"));
        return transfer;
    }

}
